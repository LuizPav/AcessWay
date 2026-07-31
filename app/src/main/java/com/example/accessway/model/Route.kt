package com.example.accessway.model

import com.google.android.gms.maps.model.LatLng

data class Route(
    val id: String,
    val name: String,
    val origin: String,
    val destination: String,
    val distanceMeters: Double,
    val durationSeconds: Double,
    val coordinates: List<LatLng> = emptyList(),
    val profile: String = "foot-walking", // "wheelchair" or "foot-walking"
    val surfaceType: String? = null,      // e.g. "asphalt", "concrete", "cobblestone"
    val maxIncline: Int? = null,         // e.g. 3, 5, 10 (%)
    val smoothness: String? = null       // e.g. "excellent", "good", "bad"
)
