package com.example.accessway.repository

import com.example.accessway.model.Stop
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

class StopRepository {

    private val db = FirebaseFirestore.getInstance()
    private val stops = db.collection("stops")

    fun addStop(
        stop: Stop,
        onSuccess: () -> Unit
    ) {

        val id = stops.document().id

        val data = hashMapOf(

            "id" to id,

            "name" to stop.name,
            "address" to stop.address,

            "latitude" to stop.location?.latitude,
            "longitude" to stop.location?.longitude,

            "avaliation" to stop.avaliation,
            "isBusStop" to stop.isBusStop,
            "reviewCount" to stop.reviewCount,

            "lines" to stop.lines,

            "ratingAcessibilidade" to stop.ratingAcessibilidade,
            "ratingPisoTatil" to stop.ratingPisoTatil,
            "ratingIluminacao" to stop.ratingIluminacao,
            "ratingCobertura" to stop.ratingCobertura,

            "ratingDistribution" to stop.ratingDistribution
        )

        stops.document(id)
            .set(data)
            .addOnSuccessListener {

                onSuccess()

            }
    }

    fun updateStop(stop: Stop) {

        val data = hashMapOf(

            "id" to stop.id,

            "name" to stop.name,
            "address" to stop.address,

            "latitude" to stop.location?.latitude,
            "longitude" to stop.location?.longitude,

            "avaliation" to stop.avaliation,
            "isBusStop" to stop.isBusStop,
            "reviewCount" to stop.reviewCount,

            "lines" to stop.lines,

            "ratingAcessibilidade" to stop.ratingAcessibilidade,
            "ratingPisoTatil" to stop.ratingPisoTatil,
            "ratingIluminacao" to stop.ratingIluminacao,
            "ratingCobertura" to stop.ratingCobertura,

            "ratingDistribution" to stop.ratingDistribution
        )

        stops.document(stop.id)
            .set(data)
    }

    fun getStops(
        onResult: (List<Stop>) -> Unit
    ) {

        stops.get()
            .addOnSuccessListener { querySnapshot ->

                val list = mutableListOf<Stop>()

                for (document in querySnapshot.documents) {

                    val stop = Stop(

                        id = document.id,

                        name = document.getString("name") ?: "",

                        address = document.getString("address") ?: "",

                        avaliation =
                            document.getDouble("avaliation")
                                ?.toFloat() ?: 0f,

                        location = LatLng(
                            document.getDouble("latitude") ?: 0.0,
                            document.getDouble("longitude") ?: 0.0
                        ),

                        isBusStop =
                            document.getBoolean("isBusStop") ?: false,

                        lines =
                            document.get("lines") as? List<String>
                                ?: emptyList(),

                        reviewCount =
                            document.getLong("reviewCount")
                                ?.toInt() ?: 0,

                        ratingAcessibilidade =
                            document.getLong("ratingAcessibilidade")
                                ?.toInt() ?: 0,

                        ratingPisoTatil =
                            document.getLong("ratingPisoTatil")
                                ?.toInt() ?: 0,

                        ratingIluminacao =
                            document.getLong("ratingIluminacao")
                                ?.toInt() ?: 0,

                        ratingCobertura =
                            document.getLong("ratingCobertura")
                                ?.toInt() ?: 0,

                        ratingDistribution =
                            (document.get("ratingDistribution") as? List<*>)
                                ?.mapNotNull { (it as? Long)?.toInt() }
                                ?: emptyList()
                    )

                    list.add(stop)
                }

                onResult(list)
            }
    }
}