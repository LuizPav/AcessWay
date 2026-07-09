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
import android.util.Log

class ProfileViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()

    var profileState by mutableStateOf(UserProfile())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isEditingName by mutableStateOf(false)

    // Dentro do seu ProfileViewModel
    init {
        loadProfile()
    }

    fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return

        // Mostra que está carregando
        isLoading = true

        viewModelScope.launch {
            userRepository.getUser(uid)
                .onSuccess { user ->
                    // Aqui garantimos que o estado foi atualizado com o que está no banco
                    profileState = user
                    isLoading = false
                }
                .onFailure {
                    Log.e("ProfileDebug", "Falha ao carregar perfil: ${it.message}")
                    isLoading = false
                }
        }
    }

    private fun updateProfile(updatedProfile: UserProfile) {
        profileState = updatedProfile
        Log.d("ProfileDebug", "Tentando salvar: $updatedProfile") // ADICIONE ISSO
        viewModelScope.launch {
            val result = userRepository.saveUser(updatedProfile)
            result.onFailure {
                Log.e("ProfileDebug", "ERRO NO FIRESTORE: ${it.message}") // ADICIONE ISSO
            }
            result.onSuccess {
                Log.d("ProfileDebug", "SUCESSO AO SALVAR!") // ADICIONE ISSO
            }
        }
    }

    fun updateName(newName: String) {
        // Garantimos que o UID vindo do Auth seja atribuído aqui
        val currentUid = auth.currentUser?.uid ?: return
        updateProfile(profileState.copy(name = newName, uid = currentUid))
    }

    fun updateWheelchair(needed: Boolean) = updateProfile(profileState.copy(needsWheelchair = needed))
    fun updateTactilePaving(needed: Boolean) = updateProfile(profileState.copy(needsTactilePaving = needed))
    fun updateAudioAlerts(needed: Boolean) = updateProfile(profileState.copy(needsAudioAlerts = needed))

    fun toggleEditNameDialog(show: Boolean) { isEditingName = show }

    fun sendPasswordResetEmail(onResult: (Boolean, String?) -> Unit) {
        val email = auth.currentUser?.email ?: return
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, if (task.isSuccessful) "E-mail enviado!" else "Falha ao enviar.")
            }
    }
}