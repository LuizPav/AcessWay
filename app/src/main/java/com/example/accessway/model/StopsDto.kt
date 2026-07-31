package com.example.accessway.model

import com.google.android.gms.maps.model.LatLng

data class Stop(
    val name: String,
    val address: String = "Recife, PE",
    val avaliation: Float = 0.0f,
    val location: LatLng? = null,
    val isBusStop: Boolean = false,
    val lines: List<String> = emptyList(),
    val reviewCount: Int = 0,
    val ratingAcessibilidade: Int = 0, // 0 = Sem avaliações, 1 = Ruim, 2 = Parcial, 3 = Bom
    val ratingPisoTatil: Int = 0,
    val ratingIluminacao: Int = 0,
    val ratingCobertura: Int = 0,
    val ratingDistribution: List<Int> = listOf(0, 0, 0, 0, 0), // 1 to 5 stars counts
    val id: String = ""
)

