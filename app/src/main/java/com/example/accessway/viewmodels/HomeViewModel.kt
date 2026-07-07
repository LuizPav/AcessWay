package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accessway.model.Stop
import com.google.android.gms.maps.model.LatLng

data class UserStopEvaluation(
    val ratingAcessibilidade: Int,
    val ratingPisoTatil: Int,
    val ratingIluminacao: Int,
    val ratingCobertura: Int,
    val userStars: Int
)

class HomeViewModel : ViewModel() {

    private val _stops = mutableStateListOf<Stop>()

    val stops: List<Stop>
        get() = _stops

    var selectedStop by mutableStateOf<Stop?>(null)

    // In-memory user evaluations per stop (keyed by stop name)
    val userEvaluations = mutableStateMapOf<String, UserStopEvaluation>()

    init {
        // Mock bus stops in Recife with detailed accessibility attributes and coordinates
        _stops.add(
            Stop(
                name = "Parada Metrô Recife",
                address = "Cais de Santa Rita, s/n - São José",
                avaliation = 4.5f,
                location = LatLng(-8.058300, -34.884800),
                isBusStop = true,
                lines = listOf("116 - Circular (Príncipe)", "224 - UR-11 / Derby", "166 - TI Cajueiro Seco"),
                reviewCount = 84,
                ratingAcessibilidade = 3, // Bom
                ratingPisoTatil = 2,       // Parcial
                ratingIluminacao = 3,      // Bom
                ratingCobertura = 1,       // Ruim/Ausente
                ratingDistribution = listOf(5, 7, 12, 30, 30)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Treze de Maio (Parque)",
                address = "Av. Visconde de Suassuna, 150 - Santo Amaro",
                avaliation = 3.8f,
                location = LatLng(-8.054200, -34.881300),
                isBusStop = true,
                lines = listOf("522 - Dois Irmãos (Rui Barbosa)", "644 - Largo do Maracanã", "741 - Dois Unidos"),
                reviewCount = 45,
                ratingAcessibilidade = 2,
                ratingPisoTatil = 1,
                ratingIluminacao = 3,
                ratingCobertura = 2,
                ratingDistribution = listOf(5, 8, 12, 10, 10)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Av. Conde da Boa Vista",
                address = "Av. Conde da Boa Vista, 450 - Boa Vista",
                avaliation = 4.2f,
                location = LatLng(-8.059400, -34.888500),
                isBusStop = true,
                lines = listOf("101 - Circular (Conde da Boa Vista)", "1983 - Rio Doce / Princesa Isabel", "2437 - TI Caxangá"),
                reviewCount = 152,
                ratingAcessibilidade = 3,
                ratingPisoTatil = 3,
                ratingIluminacao = 2,
                ratingCobertura = 1,
                ratingDistribution = listOf(10, 12, 20, 50, 60)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Praça do Derby",
                address = "Praça do Derby, s/n - Derby",
                avaliation = 4.7f,
                location = LatLng(-8.056600, -34.900900),
                isBusStop = true,
                lines = listOf("2040 - CDU / Caxangá / Boa Viagem", "050 - PE-15 / Boa Viagem", "2480 - TI Camaragibe / Derby"),
                reviewCount = 210,
                ratingAcessibilidade = 3,
                ratingPisoTatil = 3,
                ratingIluminacao = 3,
                ratingCobertura = 3,
                ratingDistribution = listOf(2, 8, 15, 65, 120)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Cais de Santa Rita",
                address = "Av. Alfredo Lisboa, s/n - Recife Antigo",
                avaliation = 2.9f,
                location = LatLng(-8.066700, -34.878900),
                isBusStop = true,
                lines = listOf("191 - IPSEP (Cônego Roma)", "107 - Circular (Cabugá / Prefeitura)", "032 - Setúbal"),
                reviewCount = 64,
                ratingAcessibilidade = 1,
                ratingPisoTatil = 1,
                ratingIluminacao = 2,
                ratingCobertura = 2,
                ratingDistribution = listOf(20, 15, 15, 10, 4)
            )
        )
        _stops.add(
            Stop(
                name = "Parada Marco Zero",
                address = "Av. Rio Branco, 20 - Recife Antigo",
                avaliation = 4.6f,
                location = LatLng(-8.063100, -34.871100),
                isBusStop = true,
                lines = listOf("032 - Setúbal (Conde da Boa Vista)", "018 - Brasília Teimosa", "014 - Brasília Teimosa"),
                reviewCount = 128,
                ratingAcessibilidade = 3,
                ratingPisoTatil = 2,
                ratingIluminacao = 3,
                ratingCobertura = 3,
                ratingDistribution = listOf(3, 5, 20, 45, 55)
            )
        )
    }

    fun removeStop(stop: Stop) {
        _stops.remove(stop)
    }

    fun addStop(stop: Stop) {
        _stops.add(stop)
    }

    fun registerPoint(location: LatLng) {
        _stops.add(
            Stop(
                name = "Nova Parada de Ônibus",
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

            if (selectedStop?.name == stopName) {
                selectedStop = updatedStop
            }
        }
    }
}