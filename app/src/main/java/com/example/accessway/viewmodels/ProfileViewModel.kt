package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accessway.model.UserProfile
import com.example.accessway.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    var isEditingName by mutableStateOf(false)

    fun toggleEditNameDialog(show: Boolean) {
        isEditingName = show
    }
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()

    var profileState by mutableStateOf(UserProfile())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadProfile()
    }


    fun sendPasswordResetEmail(onResult: (Boolean, String?) -> Unit) {
        val email = auth.currentUser?.email
        if (email == null) {
            onResult(false, "Erro: E-mail não encontrado.")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "E-mail de redefinição enviado!")
                } else {
                    onResult(false, "Falha ao enviar e-mail.")
                }
            }
    }

    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            isLoading = true
            userRepository.getUser(uid).onSuccess {
                profileState = it
            }
            isLoading = false
        }
    }

    // Função genérica de atualização para manter o código limpo
    private fun updateProfile(updatedProfile: UserProfile) {
        profileState = updatedProfile
        viewModelScope.launch {
            userRepository.saveUser(updatedProfile)
        }
    }

    fun updateWheelchair(needed: Boolean) = updateProfile(profileState.copy(needsWheelchair = needed))
    fun updateTactilePaving(needed: Boolean) = updateProfile(profileState.copy(needsTactilePaving = needed))
    fun updateAudioAlerts(needed: Boolean) = updateProfile(profileState.copy(needsAudioAlerts = needed))
    fun updateName(newName: String) = updateProfile(profileState.copy(name = newName))
}