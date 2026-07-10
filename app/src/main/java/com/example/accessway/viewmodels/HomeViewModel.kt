package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accessway.model.Stop
import com.example.accessway.network.RetrofitClient
import com.example.accessway.repository.FavoriteStopsRepository
import com.example.accessway.repository.AuthRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import android.util.Log

data class UserStopEvaluation(
    val ratingAcessibilidade: Int,
    val ratingPisoTatil: Int,
    val ratingIluminacao: Int,
    val ratingCobertura: Int,
    val userStars: Int
)

class HomeViewModel : ViewModel() {

    private val favoriteStopsRepository = FavoriteStopsRepository()
    private val authRepository = AuthRepository()
    private val communityStopsRepository = com.example.accessway.repository.CommunityStopsRepository()

    val favoriteStops = mutableStateListOf<Stop>()

    fun loadFavoriteStops() {
        val uid = authRepository.getCurrentUserUid() ?: return
        viewModelScope.launch {
            favoriteStopsRepository.getFavoriteStops(uid)
                .onSuccess { favorites ->
                    val stopIds = favorites.map { it.id.ifEmpty { it.name } }
                    val evaluationsResult = communityStopsRepository.getEvaluations(stopIds)
                    
                    val evaluatedFavorites = favorites.map { stop ->
                        val stopId = stop.id.ifEmpty { stop.name }
                        val eval = evaluationsResult.getOrNull()?.get(stopId)
                        if (eval != null) {
                            stop.copy(
                                avaliation = eval.avaliation,
                                reviewCount = eval.reviewCount,
                                ratingAcessibilidade = eval.ratingAcessibilidade,
                                ratingPisoTatil = eval.ratingPisoTatil,
                                ratingIluminacao = eval.ratingIluminacao,
                                ratingCobertura = eval.ratingCobertura,
                                ratingDistribution = eval.ratingDistribution
                            )
                        } else {
                            stop
                        }
                    }
                    favoriteStops.clear()
                    favoriteStops.addAll(evaluatedFavorites)
                }
                .onFailure {
                    Log.e("HomeViewModel", "Error loading favorites from Firestore", it)
                }
        }
    }

    fun isFavorite(stop: Stop): Boolean {
        val stopId = stop.id.ifEmpty { stop.name }
        return favoriteStops.any { it.id == stopId || it.name == stop.name }
    }

    fun toggleFavorite(stop: Stop) {
        val uid = authRepository.getCurrentUserUid() ?: return
        val stopId = stop.id.ifEmpty { stop.name }
        val currentlyFavorite = isFavorite(stop)

        viewModelScope.launch {
            if (currentlyFavorite) {
                favoriteStopsRepository.removeFavoriteStop(uid, stopId)
                    .onSuccess {
                        favoriteStops.removeAll { it.id == stopId || it.name == stop.name }
                    }
                    .onFailure {
                        Log.e("HomeViewModel", "Error removing favorite stop", it)
                    }
            } else {
                favoriteStopsRepository.saveFavoriteStop(uid, stop)
                    .onSuccess {
                        val favoriteStop = stop.copy(id = stopId)
                        favoriteStops.add(favoriteStop)
                    }
                    .onFailure {
                        Log.e("HomeViewModel", "Error saving favorite stop", it)
                    }
            }
        }
    }

    fun loadStopsFromApi(lat: Double, lon: Double, radius: Int = 1000) {
        viewModelScope.launch {
            try {
                // Ensure we have a valid token first
                if (com.example.accessway.network.TokenManager.token.isNullOrEmpty()) {
                    val cpf = com.example.accessway.BuildConfig.STOPS_API_CPF
                    val password = com.example.accessway.BuildConfig.STOPS_API_PASSWORD
                    val loginRes = RetrofitClient.instance.login(
                        com.example.accessway.model.LoginRequest(cpf, password)
                    )
                    com.example.accessway.network.TokenManager.token = loginRes.token
                }

                val apiStops = RetrofitClient.instance.getStops(lat, lon, radius)
                val newStops = apiStops.map { apiStop ->
                    Stop(
                        id = apiStop.id,
                        name = apiStop.name,
                        location = LatLng(apiStop.latitude, apiStop.longitude),
                        isBusStop = true
                    )
                }

                val stopIds = newStops.map { it.id.ifEmpty { it.name } }
                val evaluationsResult = communityStopsRepository.getEvaluations(stopIds)

                val evaluatedStops = newStops.map { stop ->
                    val stopId = stop.id.ifEmpty { stop.name }
                    val eval = evaluationsResult.getOrNull()?.get(stopId)
                    if (eval != null) {
                        stop.copy(
                            avaliation = eval.avaliation,
                            reviewCount = eval.reviewCount,
                            ratingAcessibilidade = eval.ratingAcessibilidade,
                            ratingPisoTatil = eval.ratingPisoTatil,
                            ratingIluminacao = eval.ratingIluminacao,
                            ratingCobertura = eval.ratingCobertura,
                            ratingDistribution = eval.ratingDistribution
                        )
                    } else {
                        stop
                    }
                }

                evaluatedStops.forEach { stop ->
                    val idx = _stops.indexOfFirst { it.id == stop.id || (it.name == stop.name && it.id.isEmpty()) }
                    if (idx != -1) {
                        _stops[idx] = stop
                    } else {
                        _stops.add(stop)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading stops from Retrofit", e)
            }
        }
    }

    private val _stops = mutableStateListOf<Stop>()

    val stops: List<Stop>
        get() = _stops

    var selectedStop by mutableStateOf<Stop?>(null)

    // In-memory user evaluations per stop (keyed by stop name)
    val userEvaluations = mutableStateMapOf<String, UserStopEvaluation>()

    init {
        // Mock bus stops in Recife with same metadata structure as registered ones
        _stops.add(
            Stop(
                name = "Parada Metrô Recife",
                address = "Recife, PE",
                avaliation = 4.0f,
                location = LatLng(-8.058300, -34.884800),
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
                reviewCount = 1,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 2,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(0, 0, 0, 1, 0)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Treze de Maio (Parque)",
                address = "Recife, PE",
                avaliation = 4.0f,
                location = LatLng(-8.054200, -34.881300),
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
                reviewCount = 1,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 2,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(0, 0, 0, 1, 0)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Av. Conde da Boa Vista",
                address = "Recife, PE",
                avaliation = 4.0f,
                location = LatLng(-8.059400, -34.888500),
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
                reviewCount = 1,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 2,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(0, 0, 0, 1, 0)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Praça do Derby",
                address = "Recife, PE",
                avaliation = 4.0f,
                location = LatLng(-8.056600, -34.900900),
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
                reviewCount = 1,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 2,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(0, 0, 0, 1, 0)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Cais de Santa Rita",
                address = "Recife, PE",
                avaliation = 4.0f,
                location = LatLng(-8.066700, -34.878900),
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
                reviewCount = 1,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 2,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(0, 0, 0, 1, 0)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Marco Zero",
                address = "Recife, PE",
                avaliation = 4.0f,
                location = LatLng(-8.063100, -34.871100),
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
                reviewCount = 1,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 2,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(0, 0, 0, 1, 0)
            )
        )
        loadFavoriteStops()
    }

    fun removeStop(stop: Stop) {
        _stops.remove(stop)
    }

    fun addStop(stop: Stop) {
        _stops.add(stop)
    }

    fun registerPoint(name: String, location: LatLng) {
        _stops.add(
            Stop(
                name = name,
                address = "Recife, PE",
                avaliation = 4.0f,
                location = location,
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
                reviewCount = 1,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 2,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(0, 0, 0, 1, 0)
            )
        )
    }

    fun submitEvaluation(
        stopName: String,
        acessibilidade: Int,
        pisoTatil: Int,
        iluminacao: Int,
        cobertura: Int,
        userStars: Int
    ) {
        val oldEval = userEvaluations[stopName]
        val newEval = UserStopEvaluation(acessibilidade, pisoTatil, iluminacao, cobertura, userStars)
        userEvaluations[stopName] = newEval

        val index = _stops.indexOfFirst { it.name == stopName }
        if (index != -1) {
            val stop = _stops[index]

            // Calculate new review count
            val isNewReview = oldEval == null
            val newReviewCount = if (isNewReview) stop.reviewCount + 1 else stop.reviewCount

            // Update rating distribution
            val mutableDist = stop.ratingDistribution.toMutableList()
            if (!isNewReview && oldEval != null) {
                val oldStarIdx = (oldEval.userStars - 1).coerceIn(0, 4)
                mutableDist[oldStarIdx] = (mutableDist[oldStarIdx] - 1).coerceAtLeast(0)
            }
            val newStarIdx = (userStars - 1).coerceIn(0, 4)
            mutableDist[newStarIdx] = mutableDist[newStarIdx] + 1

            // Re-calculate average rating
            var totalStars = 0f
            var totalCount = 0
            for (i in 0..4) {
                totalStars += mutableDist[i] * (i + 1)
                totalCount += mutableDist[i]
            }
            val newAverageRating = if (totalCount > 0) totalStars / totalCount else userStars.toFloat()

            val updatedStop = stop.copy(
                avaliation = (Math.round(newAverageRating * 10f) / 10f),
                reviewCount = newReviewCount,
                ratingAcessibilidade = acessibilidade,
                ratingPisoTatil = pisoTatil,
                ratingIluminacao = iluminacao,
                ratingCobertura = cobertura,
                ratingDistribution = mutableDist
            )

            _stops[index] = updatedStop

            val favIndex = favoriteStops.indexOfFirst { it.id == updatedStop.id || (it.name == updatedStop.name && it.id.isEmpty()) }
            if (favIndex != -1) {
                favoriteStops[favIndex] = updatedStop
            }

            if (selectedStop?.name == stopName) {
                selectedStop = updatedStop
            }

            val stopId = updatedStop.id.ifEmpty { updatedStop.name }
            viewModelScope.launch {
                val evaluation = com.example.accessway.repository.StopEvaluation(
                    id = stopId,
                    avaliation = updatedStop.avaliation,
                    reviewCount = updatedStop.reviewCount,
                    ratingAcessibilidade = updatedStop.ratingAcessibilidade,
                    ratingPisoTatil = updatedStop.ratingPisoTatil,
                    ratingIluminacao = updatedStop.ratingIluminacao,
                    ratingCobertura = updatedStop.ratingCobertura,
                    ratingDistribution = updatedStop.ratingDistribution
                )
                communityStopsRepository.saveEvaluation(stopId, evaluation)
            }
        }
    }
}