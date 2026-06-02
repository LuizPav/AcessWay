package com.example.accessway.viewmodels

import androidx.lifecycle.ViewModel
import com.example.accessway.model.Stop

class HomeViewModel : ViewModel() {
    private var _stops = List(10) { i ->
        Stop("Parada - $i", avaliation = i+1)
    }.toMutableList()

    val stops get() = _stops.toList()

    fun removeStop(s: Stop) {
        _stops.remove(s);
    }

    fun addStop(s: Stop) {
        _stops.add(Stop(name = s.name, avaliation =  s.avaliation))
    }
}
