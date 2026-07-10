package com.example.accessway.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CommunityStopsRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getEvaluations(stopIds: List<String>): Result<Map<String, StopEvaluation>> {
        if (stopIds.isEmpty()) return Result.success(emptyMap())
        return try {
            val resultMap = mutableMapOf<String, StopEvaluation>()
            // Firestore whereIn supports up to 30 elements
            val chunks = stopIds.chunked(30)
            for (chunk in chunks) {
                val snapshot = firestore.collection("stops")
                    .whereIn("id", chunk)
                    .get()
                    .await()
                for (doc in snapshot.documents) {
                    val id = doc.getString("id") ?: doc.id
                    val avaliation = doc.getDouble("avaliation")?.toFloat() ?: 4.0f
                    val reviewCount = doc.getLong("reviewCount")?.toInt() ?: 0
                    val ratingAcessibilidade = doc.getLong("ratingAcessibilidade")?.toInt() ?: 2
                    val ratingPisoTatil = doc.getLong("ratingPisoTatil")?.toInt() ?: 2
                    val ratingIluminacao = doc.getLong("ratingIluminacao")?.toInt() ?: 2
                    val ratingCobertura = doc.getLong("ratingCobertura")?.toInt() ?: 2
                    val ratingDistribution = (doc.get("ratingDistribution") as? List<*>)?.mapNotNull { (it as? Number)?.toInt() } ?: listOf(0, 0, 0, 0, 0)
                    
                    resultMap[id] = StopEvaluation(
                        id = id,
                        avaliation = avaliation,
                        reviewCount = reviewCount,
                        ratingAcessibilidade = ratingAcessibilidade,
                        ratingPisoTatil = ratingPisoTatil,
                        ratingIluminacao = ratingIluminacao,
                        ratingCobertura = ratingCobertura,
                        ratingDistribution = ratingDistribution
                    )
                }
            }
            Result.success(resultMap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveEvaluation(stopId: String, evaluation: StopEvaluation): Result<Unit> {
        return try {
            val data = mapOf(
                "id" to stopId,
                "avaliation" to evaluation.avaliation,
                "reviewCount" to evaluation.reviewCount,
                "ratingAcessibilidade" to evaluation.ratingAcessibilidade,
                "ratingPisoTatil" to evaluation.ratingPisoTatil,
                "ratingIluminacao" to evaluation.ratingIluminacao,
                "ratingCobertura" to evaluation.ratingCobertura,
                "ratingDistribution" to evaluation.ratingDistribution
            )
            firestore.collection("stops")
                .document(stopId)
                .set(data)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class StopEvaluation(
    val id: String = "",
    val avaliation: Float = 4.0f,
    val reviewCount: Int = 0,
    val ratingAcessibilidade: Int = 2,
    val ratingPisoTatil: Int = 2,
    val ratingIluminacao: Int = 2,
    val ratingCobertura: Int = 2,
    val ratingDistribution: List<Int> = listOf(0, 0, 0, 0, 0)
)
