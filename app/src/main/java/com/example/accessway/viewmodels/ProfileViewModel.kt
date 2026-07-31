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

private const val TAG = "ProfileViewModel"

class ProfileViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()

    var profileState by mutableStateOf(UserProfile())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isEditingName by mutableStateOf(false)

    init {
        loadProfile()
    }

    fun loadProfile() {
        val currentUser = auth.currentUser
        val uid = currentUser?.uid ?: return
        Log.d(TAG, "loadProfile called for uid=$uid")

        val email = currentUser.email ?: ""
        val fallbackName = currentUser.displayName?.ifEmpty { null }
            ?: email.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }

        if (profileState.uid != uid || profileState.name.isEmpty()) {
            profileState = UserProfile(
                uid = uid,
                email = email,
                name = fallbackName
            )
        }

        isLoading = true

        viewModelScope.launch {
            userRepository.getUser(uid)
                .onSuccess { user ->
                    profileState = user.copy(
                        name = user.name.ifEmpty { fallbackName },
                        email = user.email.ifEmpty { email }
                    )
                    isLoading = false
                    Log.d(TAG, "Successfully loaded profile for ${user.name}")
                }
                .onFailure {
                    Log.e(TAG, "Failed to load profile for uid=$uid", it)
                    isLoading = false
                }
        }
    }

    private fun updateProfile(updatedProfile: UserProfile) {
        profileState = updatedProfile
        Log.d(TAG, "Updating user profile: $updatedProfile")
        viewModelScope.launch {
            userRepository.saveUser(updatedProfile)
                .onSuccess {
                    Log.d(TAG, "Successfully updated user profile in Firestore")
                }
                .onFailure {
                    Log.e(TAG, "Error updating profile in Firestore", it)
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
    fun updateSearchRadius(radius: Int) = updateProfile(profileState.copy(searchRadius = radius))

    fun toggleEditNameDialog(show: Boolean) { isEditingName = show }

    fun sendPasswordResetEmail(onResult: (Boolean, String?) -> Unit) {
        val email = auth.currentUser?.email ?: return
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, if (task.isSuccessful) "E-mail enviado!" else "Falha ao enviar.")
            }
    }
}