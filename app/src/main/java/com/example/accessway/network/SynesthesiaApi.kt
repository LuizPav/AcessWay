package com.example.accessway.network

import com.example.accessway.model.StopResponse
import retrofit2.http.GET

interface SynesthesiaApi {

    @GET("points")
    suspend fun getStops(): List<StopResponse>

}