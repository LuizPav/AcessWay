package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accessway.model.Stop
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.viewModelScope
import com.example.accessway.repository.SynesthesiaRepository
import kotlinx.coroutines.launch
import com.example.accessway.model.Review
import com.example.accessway.repository.AuthRepository
import com.example.accessway.repository.ReviewRepository

data class UserStopEvaluation(
    val ratingAcessibilidade: Int,
    val ratingPisoTatil: Int,
    val ratingIluminacao: Int,
    val ratingCobertura: Int,
    val userStars: Int
)

class HomeViewModel : ViewModel() {

    private val _stops = mutableStateListOf<Stop>()

    private val reviewRepository = ReviewRepository()

    private val authRepository = AuthRepository()

    private val repository = SynesthesiaRepository()
    val stops: List<Stop>
        get() = _stops

    var selectedStop by mutableStateOf<Stop?>(null)

    // In-memory user evaluations per stop (keyed by stop name)
    val userEvaluations = mutableStateMapOf<String, UserStopEvaluation>()

    init {
        loadStops()
    }

    private fun loadStops() {

        viewModelScope.launch {

            try {

                val list = repository.getStops()

                _stops.clear()

                _stops.addAll(list)

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }


    fun submitEvaluation(
        stopId: String,
        acessibilidade: Int,
        pisoTatil: Int,
        iluminacao: Int,
        cobertura: Int,
        userStars: Int
    ) {

        val review = Review(

            stopId = stopId,

            userId = authRepository.getCurrentUserUid() ?: "",

            stars = userStars,

            acessibilidade = acessibilidade,

            pisoTatil = pisoTatil,

            iluminacao = iluminacao,

            cobertura = cobertura

        )

        reviewRepository.saveReview(

            review,

            onSuccess = {

                println("Avaliação salva!")

            },

            onError = {

                it.printStackTrace()

            }

        )

    }
}