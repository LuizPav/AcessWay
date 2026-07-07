package com.example.accessway.model

import com.google.android.gms.maps.model.LatLng

data class Stop(
    val name: String,
    val avaliation: Int,
    val location: LatLng? = null,
    val isBusStop: Boolean = false,
    val lines: List<String> = emptyList()
)
