package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accessway.model.Route
import com.google.android.gms.maps.model.LatLng

class RoutesViewModel : ViewModel() {

    private val allRoutes = listOf(
        Route(
            id = "1",
            name = "Rota Rápida Av. Central",
            origin = "Terminal Central",
            destination = "Shopping Plaza",
            distanceMeters = 1200.0,
            durationSeconds = 900.0,
            profile = "foot-walking",
            surfaceType = "asphalt",
            maxIncline = 2,
            smoothness = "good",
            coordinates = listOf(
                LatLng(-23.550520, -46.633308),
                LatLng(-23.551520, -46.634308)
            )
        ),
        Route(
            id = "2",
            name = "Acesso Suave com Rampa - Av. das Flores",
            origin = "Terminal Central",
            destination = "Hospital Municipal",
            distanceMeters = 1500.0,
            durationSeconds = 1200.0,
            profile = "wheelchair",
            surfaceType = "concrete",
            maxIncline = 3,
            smoothness = "excellent",
            coordinates = listOf(
                LatLng(-23.550520, -46.633308),
                LatLng(-23.552520, -46.635308)
            )
        ),
        Route(
            id = "3",
            name = "Caminho Acessível Parque Ecológico",
            origin = "Estação Parque",
            destination = "Entrada Principal",
            distanceMeters = 850.0,
            durationSeconds = 640.0,
            profile = "wheelchair",
            surfaceType = "paving_stones",
            maxIncline = 1,
            smoothness = "good",
            coordinates = listOf(
                LatLng(-23.560000, -46.640000),
                LatLng(-23.562000, -46.642000)
            )
        ),
        Route(
            id = "4",
            name = "Acesso Direto Rua da Consolação",
            origin = "Metrô Consolação",
            destination = "Praça Roosevelt",
            distanceMeters = 1100.0,
            durationSeconds = 850.0,
            profile = "foot-walking",
            surfaceType = "asphalt",
            maxIncline = 6,
            smoothness = "good",
            coordinates = listOf(
                LatLng(-23.558000, -46.650000),
                LatLng(-23.552000, -46.642000)
            )
        ),
        Route(
            id = "5",
            name = "Rota Elevada Acessível Metrô",
            origin = "Metrô Consolação",
            destination = "Teatro Municipal",
            distanceMeters = 2200.0,
            durationSeconds = 1800.0,
            profile = "wheelchair",
            surfaceType = "concrete",
            maxIncline = 4,
            smoothness = "excellent",
            coordinates = listOf(
                LatLng(-23.558000, -46.650000),
                LatLng(-23.545000, -46.635000)
            )
        )
    )

    var searchQuery by mutableStateOf("")
        private set

    var selectedProfile by mutableStateOf("wheelchair") // Defaulting to wheelchair for accessibility focus
        private set

    val filteredRoutes: List<Route>
        get() = allRoutes.filter { route ->
            route.profile == selectedProfile &&
                    (route.name.contains(searchQuery, ignoreCase = true) ||
                            route.origin.contains(searchQuery, ignoreCase = true) ||
                            route.destination.contains(searchQuery, ignoreCase = true))
        }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun onProfileChange(profile: String) {
        selectedProfile = profile
    }
}
