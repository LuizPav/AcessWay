package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accessway.model.UserProfile

class ProfileViewModel : ViewModel() {

    var profileState by mutableStateOf(
        UserProfile(
            uid = "user_12345",
            name = "Luiz Pavão",
            email = "luiz.pavao@example.com",
            avatarUrl = null,
            needsWheelchair = true,
            needsTactilePaving = false,
            needsAudioAlerts = true
        )
    )
        private set

    fun updateWheelchair(needed: Boolean) {
        profileState = profileState.copy(needsWheelchair = needed)
    }

    fun updateTactilePaving(needed: Boolean) {
        profileState = profileState.copy(needsTactilePaving = needed)
    }

    fun updateAudioAlerts(needed: Boolean) {
        profileState = profileState.copy(needsAudioAlerts = needed)
    }

    fun updateName(newName: String) {
        profileState = profileState.copy(name = newName)
    }
}
