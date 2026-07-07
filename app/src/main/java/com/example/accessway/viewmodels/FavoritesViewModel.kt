package com.example.accessway.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.accessway.model.Favorite

class FavoritesViewModel : ViewModel() {

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

    fun removeFavorite(id: String) {
        _favoritesList.removeAll { it.id == id }
    }

    fun addFavorite(favorite: Favorite) {
        _favoritesList.add(favorite)
    }
}
