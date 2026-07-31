package com.example.accessway.repository

import android.util.Log
import com.example.accessway.model.Stop
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "FavoriteStopsRepo"

class FavoriteStopsRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveFavoriteStop(userId: String, stop: Stop): Result<Unit> {
        val stopId = stop.id.ifEmpty { stop.name }
        Log.d(TAG, "saveFavoriteStop called for userId=$userId, stopId=$stopId")
        return try {
            val data = mapOf(
                "id" to stopId,
                "name" to stop.name,
                "address" to stop.address,
                "avaliation" to stop.avaliation,
                "latitude" to (stop.location?.latitude ?: 0.0),
                "longitude" to (stop.location?.longitude ?: 0.0),
                "isBusStop" to stop.isBusStop,
                "lines" to stop.lines,
                "reviewCount" to stop.reviewCount,
                "ratingAcessibilidade" to stop.ratingAcessibilidade,
                "ratingPisoTatil" to stop.ratingPisoTatil,
                "ratingIluminacao" to stop.ratingIluminacao,
                "ratingCobertura" to stop.ratingCobertura,
                "ratingDistribution" to stop.ratingDistribution
            )
            firestore.collection("users")
                .document(userId)
                .collection("favorite_stops")
                .document(stopId)
                .set(data)
                .await()
            Log.d(TAG, "Successfully saved favorite stop: $stopId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving favorite stop: $stopId", e)
            Result.failure(e)
        }
    }

    suspend fun removeFavoriteStop(userId: String, stopId: String): Result<Unit> {
        Log.d(TAG, "removeFavoriteStop called for userId=$userId, stopId=$stopId")
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("favorite_stops")
                .document(stopId)
                .delete()
                .await()
            Log.d(TAG, "Successfully removed favorite stop: $stopId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error removing favorite stop: $stopId", e)
            Result.failure(e)
        }
    }

    suspend fun getFavoriteStops(userId: String): Result<List<Stop>> {
        Log.d(TAG, "getFavoriteStops called for userId=$userId")
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("favorite_stops")
                .get()
                .await()

            val stops = snapshot.documents.mapNotNull { doc ->
                val id = doc.getString("id") ?: doc.id
                val name = doc.getString("name") ?: ""
                val address = doc.getString("address") ?: "Recife, PE"
                val avaliation = doc.getDouble("avaliation")?.toFloat() ?: 0.0f
                val latitude = doc.getDouble("latitude") ?: 0.0
                val longitude = doc.getDouble("longitude") ?: 0.0
                val isBusStop = doc.getBoolean("isBusStop") ?: true
                val lines = (doc.get("lines") as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
                val reviewCount = doc.getLong("reviewCount")?.toInt() ?: 0
                val ratingAcessibilidade = doc.getLong("ratingAcessibilidade")?.toInt() ?: 0
                val ratingPisoTatil = doc.getLong("ratingPisoTatil")?.toInt() ?: 0
                val ratingIluminacao = doc.getLong("ratingIluminacao")?.toInt() ?: 0
                val ratingCobertura = doc.getLong("ratingCobertura")?.toInt() ?: 0
                val ratingDistribution = (doc.get("ratingDistribution") as? List<*>)?.mapNotNull { (it as? Number)?.toInt() } ?: listOf(0, 0, 0, 0, 0)

                Stop(
                    id = id,
                    name = name,
                    address = address,
                    avaliation = avaliation,
                    location = LatLng(latitude, longitude),
                    isBusStop = isBusStop,
                    lines = lines,
                    reviewCount = reviewCount,
                    ratingAcessibilidade = ratingAcessibilidade,
                    ratingPisoTatil = ratingPisoTatil,
                    ratingIluminacao = ratingIluminacao,
                    ratingCobertura = ratingCobertura,
                    ratingDistribution = ratingDistribution
                )
            }
            Log.d(TAG, "Loaded ${stops.size} favorite stops")
            Result.success(stops)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching favorite stops", e)
            Result.failure(e)
        }
    }
}
