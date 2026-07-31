package com.example.accessway.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accessway.model.Favorite
import com.example.accessway.repository.FavoriteStopsRepository
import com.example.accessway.repository.AuthRepository
import kotlinx.coroutines.launch
import android.util.Log

private const val TAG = "FavoritesViewModel"

class FavoritesViewModel : ViewModel() {

    private val favoriteStopsRepository = FavoriteStopsRepository()
    private val authRepository = AuthRepository()

    val favoriteStops = mutableStateListOf<Favorite>()

    init {
        loadFavoriteStops()
    }

    fun loadFavoriteStops() {
        val uid = authRepository.getCurrentUserUid() ?: return
        Log.d(TAG, "loadFavoriteStops called for uid=$uid")
        viewModelScope.launch {
            favoriteStopsRepository.getFavoriteStops(uid)
                .onSuccess { stops ->
                    favoriteStops.clear()
                    favoriteStops.addAll(
                        stops.map { stop ->
                            Favorite(
                                id = stop.id,
                                name = stop.name,
                                address = stop.address.ifEmpty { "Parada de ônibus" },
                                accessibilityRating = stop.ratingAcessibilidade,
                                latitude = stop.location?.latitude ?: 0.0,
                                longitude = stop.location?.longitude ?: 0.0
                            )
                        }
                    )
                    Log.d(TAG, "Loaded ${favoriteStops.size} favorite stops into ViewModel")
                }
                .onFailure {
                    Log.e(TAG, "Error loading favorite stops for uid=$uid", it)
                }
        }
    }

    fun removeFavorite(id: String) {
        Log.d(TAG, "removeFavorite called for id=$id")
        val uid = authRepository.getCurrentUserUid() ?: return
        viewModelScope.launch {
            favoriteStopsRepository.removeFavoriteStop(uid, id)
                .onSuccess {
                    favoriteStops.removeAll { it.id == id }
                    Log.d(TAG, "Successfully removed favorite stop $id from Firestore")
                }
                .onFailure {
                    Log.e(TAG, "Error removing favorite stop $id from Firestore", it)
                }
        }
    }
}
