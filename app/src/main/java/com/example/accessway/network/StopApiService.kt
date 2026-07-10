package com.example.accessway.network

import com.example.accessway.BuildConfig
import com.example.accessway.model.ApiStop
import com.example.accessway.model.LoginRequest
import com.example.accessway.model.LoginResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StopApiService {
    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("stop/{lat}/{lon}/{radius}")
    suspend fun getStops(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("radius") radius: Int
    ): List<ApiStop>
}

object TokenManager {
    var token: String? = null
}

object RetrofitClient {
    private const val BASE_URL = BuildConfig.STOPS_API_URL

    // Interceptor para adicionar o token de autorização
    private val authInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            // Não adiciona o cabeçalho se a requisição for para login
            if (originalRequest.url.encodedPath.endsWith("user/login")) {
                return chain.proceed(originalRequest)
            }

            val token = TokenManager.token
            return if (!token.isNullOrEmpty()) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "$token")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(originalRequest)
            }
        }
    }

    // Cria o interceptor que joga os dados da rede no Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cria o cliente HTTP injetando os interceptores
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: StopApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StopApiService::class.java)
    }
}
