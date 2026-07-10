package com.example.accessway.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accessway.model.Favorite
import com.example.accessway.repository.FavoriteStopsRepository
import com.example.accessway.repository.AuthRepository
import kotlinx.coroutines.launch
import android.util.Log

class FavoritesViewModel : ViewModel() {

    private val favoriteStopsRepository = FavoriteStopsRepository()
    private val authRepository = AuthRepository()

    private val _favoritesList = mutableStateListOf<Favorite>()

    val favorites: List<Favorite>
        get() = _favoritesList

    val favoriteStops = mutableStateListOf<Favorite>()

    init {
        loadFavoriteStops()
    }

    fun loadFavoriteStops() {
        val uid = authRepository.getCurrentUserUid() ?: return
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
                }
                .onFailure {
                    Log.e("FavoritesViewModel", "Error loading favorite stops", it)
                }
        }
    }

    fun removeFavorite(id: String) {
        if (_favoritesList.any { it.id == id }) {
            _favoritesList.removeAll { it.id == id }
        } else {
            val uid = authRepository.getCurrentUserUid() ?: return
            viewModelScope.launch {
                favoriteStopsRepository.removeFavoriteStop(uid, id)
                    .onSuccess {
                        favoriteStops.removeAll { it.id == id }
                    }
                    .onFailure {
                        Log.e("FavoritesViewModel", "Error removing favorite stop", it)
                    }
            }
        }
    }

    fun addFavorite(favorite: Favorite) {
        _favoritesList.add(favorite)
    }
}
