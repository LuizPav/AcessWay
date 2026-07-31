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
import com.example.accessway.repository.UserRepository
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

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    private val favoriteStopsRepository = FavoriteStopsRepository()
    private val authRepository = AuthRepository()
    private val communityStopsRepository = com.example.accessway.repository.CommunityStopsRepository()

    val favoriteStops = mutableStateListOf<Stop>()

    var searchRadius by mutableStateOf(1000)
        private set

    private var lastUid: String? = null

    private fun checkUserSession() {
        val currentUid = authRepository.getCurrentUserUid()
        if (currentUid != lastUid) {
            Log.d(TAG, "User session changed from $lastUid to $currentUid - clearing stale in-memory evaluations")
            userEvaluations.clear()
            favoriteStops.clear()
            lastUid = currentUid
        }
    }

    fun loadSearchRadius() {
        checkUserSession()
        val uid = authRepository.getCurrentUserUid() ?: return
        Log.d(TAG, "loadSearchRadius called for uid=$uid")
        viewModelScope.launch {
            UserRepository().getUser(uid)
                .onSuccess { user ->
                    searchRadius = user.searchRadius
                    Log.d(TAG, "Loaded user search radius: ${searchRadius}m")
                }
                .onFailure {
                    Log.w(TAG, "Could not load user search radius, keeping default 1000m", it)
                }
        }
    }

    fun loadFavoriteStops() {
        checkUserSession()
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

    fun loadStopsFromApi(lat: Double, lon: Double, radius: Int = searchRadius) {
        viewModelScope.launch {
            try {
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

                _stops.clear()
                _stops.addAll(evaluatedStops)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading stops from Retrofit", e)
            }
        }
    }

    fun searchStops(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            try {
                val apiStops = RetrofitClient.instance.searchPoints(name = query)
                val newStops = apiStops.take(1).map { apiStop ->
                    Stop(
                        id = apiStop.id,
                        name = apiStop.name,
                        location = LatLng(apiStop.latitude, apiStop.longitude),
                        address = apiStop.description ?: "Recife, PE",
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

                _stops.clear()
                _stops.addAll(evaluatedStops)

                if (evaluatedStops.isNotEmpty()) {
                    selectStop(evaluatedStops.first())
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error searching stops", e)
            }
        }
    }

    private val _stops = mutableStateListOf<Stop>()

    val stops: List<Stop>
        get() = _stops

    var selectedStop by mutableStateOf<Stop?>(null)

    fun selectStop(stop: Stop?) {
        checkUserSession()
        selectedStop = stop
        if (stop != null) {
            val uid = authRepository.getCurrentUserUid()
            val stopId = stop.id.ifEmpty { stop.name }
            if (uid == null) {
                userEvaluations.remove(stop.name)
                userEvaluations.remove(stopId)
                return
            }
            viewModelScope.launch {
                communityStopsRepository.getUserEvaluation(stopId, uid)
                    .onSuccess { review ->
                        if (review != null) {
                            val eval = UserStopEvaluation(
                                ratingAcessibilidade = review.ratingAcessibilidade,
                                ratingPisoTatil = review.ratingPisoTatil,
                                ratingIluminacao = review.ratingIluminacao,
                                ratingCobertura = review.ratingCobertura,
                                userStars = review.userStars
                            )
                            userEvaluations[stop.name] = eval
                            userEvaluations[stopId] = eval
                            Log.d(TAG, "Loaded evaluation for user $uid on stop $stopId: ${review.userStars} stars")
                        } else {
                            userEvaluations.remove(stop.name)
                            userEvaluations.remove(stopId)
                            Log.d(TAG, "No evaluation found for user $uid on stop $stopId")
                        }
                    }
                    .onFailure {
                        userEvaluations.remove(stop.name)
                        userEvaluations.remove(stopId)
                        Log.e(TAG, "Failed to fetch user evaluation for stop $stopId", it)
                    }
            }
        }
    }

    // In-memory user evaluations per stop (keyed by stop name)
    val userEvaluations = mutableStateMapOf<String, UserStopEvaluation>()

    init {
        loadSearchRadius()
        loadFavoriteStops()
    }

    fun removeStop(stop: Stop) {
        _stops.remove(stop)
    }

    fun addStop(stop: Stop) {
        _stops.add(stop)
    }

    fun registerPoint(name: String, location: LatLng) {
        val newStop = Stop(
            name = name,
            address = "Recife, PE",
            avaliation = 0.0f,
            location = location,
            isBusStop = true,
            lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro"),
            reviewCount = 0,
            ratingAcessibilidade = 0,
            ratingPisoTatil = 0,
            ratingIluminacao = 0,
            ratingCobertura = 0,
            ratingDistribution = listOf(0, 0, 0, 0, 0)
        )
        _stops.add(newStop)
        selectStop(newStop)
    }

    fun submitEvaluation(
        stopName: String,
        acessibilidade: Int,
        pisoTatil: Int,
        iluminacao: Int,
        cobertura: Int,
        userStars: Int
    ) {
        val uid = authRepository.getCurrentUserUid() ?: return
        val targetStop = _stops.find { it.name == stopName } ?: selectedStop ?: return
        val stopId = targetStop.id.ifEmpty { targetStop.name }

        val newEval = UserStopEvaluation(acessibilidade, pisoTatil, iluminacao, cobertura, userStars)
        userEvaluations[stopName] = newEval

        viewModelScope.launch {
            val userReview = com.example.accessway.repository.UserReview(
                userId = uid,
                userStars = userStars,
                ratingAcessibilidade = acessibilidade,
                ratingPisoTatil = pisoTatil,
                ratingIluminacao = iluminacao,
                ratingCobertura = cobertura
            )

            communityStopsRepository.saveUserEvaluation(stopId, uid, userReview)
                .onSuccess { updatedEval ->
                    val index = _stops.indexOfFirst { (it.id.ifEmpty { it.name }) == stopId || it.name == stopName }
                    if (index != -1) {
                        val current = _stops[index]
                        val updatedStop = current.copy(
                            avaliation = updatedEval.avaliation,
                            reviewCount = updatedEval.reviewCount,
                            ratingAcessibilidade = updatedEval.ratingAcessibilidade,
                            ratingPisoTatil = updatedEval.ratingPisoTatil,
                            ratingIluminacao = updatedEval.ratingIluminacao,
                            ratingCobertura = updatedEval.ratingCobertura,
                            ratingDistribution = updatedEval.ratingDistribution
                        )
                        _stops[index] = updatedStop

                        val favIndex = favoriteStops.indexOfFirst { (it.id.ifEmpty { it.name }) == stopId || it.name == stopName }
                        if (favIndex != -1) {
                            favoriteStops[favIndex] = updatedStop
                        }

                        if (selectedStop?.name == stopName || (selectedStop?.id?.ifEmpty { selectedStop?.name }) == stopId) {
                            selectedStop = updatedStop
                        }
                    }
                }
                .onFailure {
                    Log.e("HomeViewModel", "Error saving evaluation to Firestore", it)
                }
        }
    }
}