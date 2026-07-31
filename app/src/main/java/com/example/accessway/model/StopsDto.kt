package com.example.accessway.model

import com.google.android.gms.maps.model.LatLng

data class Stop(
    val id: String = "",
    val name: String,
    val address: String = "Recife, PE",
    val avaliation: Float = 4.0f,
    val location: LatLng? = null,
    val isBusStop: Boolean = false,
    val lines: List<String> = emptyList(),
    val reviewCount: Int = 10,
    val ratingAcessibilidade: Int = 2, // 1 = Ruim/Ausente, 2 = Parcial, 3 = Bom/Disponível
    val ratingPisoTatil: Int = 2,
    val ratingIluminacao: Int = 2,
    val ratingCobertura: Int = 2,
    val ratingDistribution: List<Int> = listOf(0, 1, 2, 3, 4) // 1 to 5 stars counts
)
