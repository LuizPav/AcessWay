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

    private val _favoritesList = mutableStateListOf(
        Favorite(
            id = "1",
            name = "Minha Casa",
            address = "Rua das Acacias, 123 - Centro",
            accessibilityRating = 5,
            latitude = -23.550520,
            longitude = -46.633308
        ),
        Favorite(
            id = "2",
            name = "Trabalho",
            address = "Av. Paulista, 1000 - Bela Vista",
            accessibilityRating = 4,
            latitude = -23.561500,
            longitude = -46.656000
        ),
        Favorite(
            id = "3",
            name = "Clínica Médica",
            address = "Rua Conselheiro Brotero, 456 - Higienópolis",
            accessibilityRating = 5,
            latitude = -23.538000,
            longitude = -46.662000
        ),
        Favorite(
            id = "4",
            name = "Supermercado Pão de Açúcar",
            address = "Alameda Lorena, 800 - Jardins",
            accessibilityRating = 3,
            latitude = -23.567000,
            longitude = -46.660000
        )
    )

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
