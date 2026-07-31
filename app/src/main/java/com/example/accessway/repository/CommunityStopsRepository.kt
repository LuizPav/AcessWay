package com.example.accessway.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "CommunityStopsRepo"

data class UserReview(
    val userId: String = "",
    val userStars: Int = 0,
    val ratingAcessibilidade: Int = 0,
    val ratingPisoTatil: Int = 0,
    val ratingIluminacao: Int = 0,
    val ratingCobertura: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class StopEvaluation(
    val id: String = "",
    val avaliation: Float = 0.0f,
    val reviewCount: Int = 0,
    val ratingAcessibilidade: Int = 0,
    val ratingPisoTatil: Int = 0,
    val ratingIluminacao: Int = 0,
    val ratingCobertura: Int = 0,
    val ratingDistribution: List<Int> = listOf(0, 0, 0, 0, 0)
)

class CommunityStopsRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getEvaluations(stopIds: List<String>): Result<Map<String, StopEvaluation>> {
        if (stopIds.isEmpty()) return Result.success(emptyMap())
        Log.d(TAG, "getEvaluations called for ${stopIds.size} stops")
        return try {
            val resultMap = mutableMapOf<String, StopEvaluation>()
            val chunks = stopIds.chunked(30)
            for (chunk in chunks) {
                val snapshot = firestore.collection("review")
                    .whereIn("id", chunk)
                    .get()
                    .await()
                for (doc in snapshot.documents) {
                    val id = doc.getString("id") ?: doc.id
                    val avaliation = doc.getDouble("avaliation")?.toFloat() ?: 0.0f
                    val reviewCount = doc.getLong("reviewCount")?.toInt() ?: 0
                    val ratingAcessibilidade = doc.getLong("ratingAcessibilidade")?.toInt() ?: 0
                    val ratingPisoTatil = doc.getLong("ratingPisoTatil")?.toInt() ?: 0
                    val ratingIluminacao = doc.getLong("ratingIluminacao")?.toInt() ?: 0
                    val ratingCobertura = doc.getLong("ratingCobertura")?.toInt() ?: 0
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
            Log.d(TAG, "getEvaluations loaded ${resultMap.size} evaluations from Firestore")
            Result.success(resultMap)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting evaluations from Firestore", e)
            Result.failure(e)
        }
    }

    suspend fun getUserEvaluation(stopId: String, userId: String): Result<UserReview?> {
        if (stopId.isEmpty() || userId.isEmpty()) return Result.success(null)
        Log.d(TAG, "getUserEvaluation called for stopId=$stopId, userId=$userId")
        return try {
            val doc = firestore.collection("review")
                .document(stopId)
                .collection("user_reviews")
                .document(userId)
                .get()
                .await()

            if (doc.exists()) {
                val review = UserReview(
                    userId = doc.getString("userId") ?: userId,
                    userStars = doc.getLong("userStars")?.toInt() ?: 0,
                    ratingAcessibilidade = doc.getLong("ratingAcessibilidade")?.toInt() ?: 0,
                    ratingPisoTatil = doc.getLong("ratingPisoTatil")?.toInt() ?: 0,
                    ratingIluminacao = doc.getLong("ratingIluminacao")?.toInt() ?: 0,
                    ratingCobertura = doc.getLong("ratingCobertura")?.toInt() ?: 0,
                    timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                )
                Log.d(TAG, "Found user review for stopId=$stopId: stars=${review.userStars}")
                Result.success(review)
            } else {
                Log.d(TAG, "No user review found for stopId=$stopId, userId=$userId")
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user evaluation for stopId=$stopId, userId=$userId", e)
            Result.failure(e)
        }
    }

    suspend fun saveUserEvaluation(stopId: String, userId: String, review: UserReview): Result<StopEvaluation> {
        if (stopId.isEmpty() || userId.isEmpty()) {
            val err = IllegalArgumentException("stopId e userId são obrigatórios")
            Log.e(TAG, "saveUserEvaluation failed: missing stopId or userId", err)
            return Result.failure(err)
        }
        Log.d(TAG, "saveUserEvaluation starting for stopId=$stopId, userId=$userId, stars=${review.userStars}")

        return try {
            val stopDocRef = firestore.collection("review").document(stopId)
            val userReviewRef = stopDocRef.collection("user_reviews").document(userId)

            val reviewData = mapOf(
                "userId" to userId,
                "stopId" to stopId,
                "userStars" to review.userStars,
                "ratingAcessibilidade" to review.ratingAcessibilidade,
                "ratingPisoTatil" to review.ratingPisoTatil,
                "ratingIluminacao" to review.ratingIluminacao,
                "ratingCobertura" to review.ratingCobertura,
                "timestamp" to System.currentTimeMillis()
            )
            userReviewRef.set(reviewData).await()
            Log.d(TAG, "User review successfully written to review/$stopId/user_reviews/$userId")

            // Re-aggregate ratings for stopId from all user_reviews
            var avgStars = review.userStars.toFloat()
            var reviewCount = 1
            var avgAcess = review.ratingAcessibilidade
            var avgPiso = review.ratingPisoTatil
            var avgIlum = review.ratingIluminacao
            var avgCob = review.ratingCobertura
            val starCounts = IntArray(5)
            if (review.userStars in 1..5) starCounts[review.userStars - 1] = 1

            try {
                val allReviewsSnap = stopDocRef.collection("user_reviews").get().await()
                var totalStars = 0
                var totalAcess = 0
                var totalPiso = 0
                var totalIlum = 0
                var totalCob = 0
                starCounts.fill(0)

                reviewCount = allReviewsSnap.documents.size
                for (userDoc in allReviewsSnap.documents) {
                    val stars = (userDoc.getLong("userStars")?.toInt() ?: 1).coerceIn(1, 5)
                    starCounts[stars - 1]++
                    totalStars += stars

                    totalAcess += (userDoc.getLong("ratingAcessibilidade")?.toInt() ?: 0)
                    totalPiso += (userDoc.getLong("ratingPisoTatil")?.toInt() ?: 0)
                    totalIlum += (userDoc.getLong("ratingIluminacao")?.toInt() ?: 0)
                    totalCob += (userDoc.getLong("ratingCobertura")?.toInt() ?: 0)
                }

                avgStars = if (reviewCount > 0) Math.round((totalStars.toFloat() / reviewCount) * 10f) / 10f else 0.0f
                avgAcess = if (reviewCount > 0) Math.round(totalAcess.toFloat() / reviewCount) else 0
                avgPiso = if (reviewCount > 0) Math.round(totalPiso.toFloat() / reviewCount) else 0
                avgIlum = if (reviewCount > 0) Math.round(totalIlum.toFloat() / reviewCount) else 0
                avgCob = if (reviewCount > 0) Math.round(totalCob.toFloat() / reviewCount) else 0
            } catch (aggregationErr: Exception) {
                Log.w(TAG, "Could not aggregate user_reviews for stopId=$stopId (check subcollection read permissions)", aggregationErr)
            }

            val distributionList = starCounts.toList()

            val aggregatedData = mapOf(
                "id" to stopId,
                "avaliation" to avgStars,
                "reviewCount" to reviewCount,
                "ratingAcessibilidade" to avgAcess,
                "ratingPisoTatil" to avgPiso,
                "ratingIluminacao" to avgIlum,
                "ratingCobertura" to avgCob,
                "ratingDistribution" to distributionList
            )

            try {
                stopDocRef.set(aggregatedData).await()
                Log.d(TAG, "Aggregated evaluation document updated for review/$stopId: avgStars=$avgStars, reviewCount=$reviewCount")
            } catch (parentErr: Exception) {
                Log.w(TAG, "Could not update parent document review/$stopId (check document write permissions)", parentErr)
            }

            val updatedEvaluation = StopEvaluation(
                id = stopId,
                avaliation = avgStars,
                reviewCount = reviewCount,
                ratingAcessibilidade = avgAcess,
                ratingPisoTatil = avgPiso,
                ratingIluminacao = avgIlum,
                ratingCobertura = avgCob,
                ratingDistribution = distributionList
            )

            Result.success(updatedEvaluation)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user evaluation for stopId=$stopId, userId=$userId", e)
            Result.failure(e)
        }
    }
}

