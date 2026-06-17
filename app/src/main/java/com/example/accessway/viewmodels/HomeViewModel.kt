package com.example.accessway.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.accessway.model.Stop
import com.google.android.gms.maps.model.LatLng

class HomeViewModel : ViewModel() {

    private val _stops = mutableStateListOf<Stop>()

    val stops: List<Stop>
        get() = _stops

    init {

        repeat(10) { i ->

            _stops.add(

                Stop(
                    name = "Parada - $i",
                    avaliation = i + 1
                )
            )
        }
    }

    fun removeStop(
        stop: Stop
    ) {

        _stops.remove(stop)
    }

    fun addStop(
        stop: Stop
    ) {

        _stops.add(stop)
    }

    fun registerPoint(
        location: LatLng
    ) {

        _stops.add(

            Stop(
                name = "Nova parada",
                avaliation = 0,
                location = location
            )
        )
    }
}