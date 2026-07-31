package com.example.accessway.repository

import com.example.accessway.model.Stop
import com.example.accessway.network.RetrofitInstance
import com.google.android.gms.maps.model.LatLng

class SynesthesiaRepository {

    suspend fun getStops(): List<Stop> {

        return RetrofitInstance.api.getStops()

            .map {

                Stop(

                    id = it.id,

                    name = it.name,

                    address = it.description,

                    location = LatLng(
                        it.latitude,
                        it.longitude
                    ),

                    isBusStop = true
                )
            }

    }

}