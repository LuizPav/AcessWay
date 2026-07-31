package com.example.accessway.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL =
        "https://labgeo3.recife.ifpe.edu.br/synesthesiaAPI/"

    val api: SynesthesiaApi by lazy {

        Retrofit.Builder()

            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create())

            .build()

            .create(SynesthesiaApi::class.java)

    }
}