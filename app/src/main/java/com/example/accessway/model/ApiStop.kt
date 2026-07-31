package com.example.accessway.model

import com.google.gson.annotations.SerializedName

data class ApiStop(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("description") val description: String? = null,
    @SerializedName("distance") val distance: Double? = null
)

