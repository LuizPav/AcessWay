package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accessway.model.Stop
import com.google.android.gms.maps.model.LatLng

class HomeViewModel : ViewModel() {

    private val _stops = mutableStateListOf<Stop>()

    val stops: List<Stop>
        get() = _stops

    var selectedStop by mutableStateOf<Stop?>(null)

    init {
        // Mock bus stops in Recife
        _stops.add(
            Stop(
                name = "Parada Metrô Recife",
                avaliation = 5,
                location = LatLng(-8.058300, -34.884800),
                isBusStop = true,
                lines = listOf("116 - Circular (Príncipe)", "224 - UR-11 / Derby", "166 - TI Cajueiro Seco")
            )
        )
        _stops.add(
            Stop(
                name = "Parada Treze de Maio (Parque)",
                avaliation = 4,
                location = LatLng(-8.054200, -34.881300),
                isBusStop = true,
                lines = listOf("522 - Dois Irmãos (Rui Barbosa)", "644 - Largo do Maracanã", "741 - Dois Unidos")
            )
        )
        _stops.add(
            Stop(
                name = "Parada Av. Conde da Boa Vista",
                avaliation = 4,
                location = LatLng(-8.059400, -34.888500),
                isBusStop = true,
                lines = listOf("101 - Circular (Conde da Boa Vista)", "1983 - Rio Doce / Princesa Isabel", "2437 - TI Caxangá")
            )
        )
        _stops.add(
            Stop(
                name = "Parada Praça do Derby",
                avaliation = 5,
                location = LatLng(-8.056600, -34.900900),
                isBusStop = true,
                lines = listOf("2040 - CDU / Caxangá / Boa Viagem", "050 - PE-15 / Boa Viagem", "2480 - TI Camaragibe / Derby")
            )
        )
        _stops.add(
            Stop(
                name = "Parada Cais de Santa Rita",
                avaliation = 3,
                location = LatLng(-8.066700, -34.878900),
                isBusStop = true,
                lines = listOf("191 - IPSEP (Cônego Roma)", "107 - Circular (Cabugá / Prefeitura)", "032 - Setúbal")
            )
        )
        _stops.add(
            Stop(
                name = "Parada Marco Zero",
                avaliation = 5,
                location = LatLng(-8.063100, -34.871100),
                isBusStop = true,
                lines = listOf("032 - Setúbal (Conde da Boa Vista)", "018 - Brasília Teimosa", "014 - Brasília Teimosa")
            )
        )
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
                name = "Nova Parada de Ônibus",
                avaliation = 4,
                location = location,
                isBusStop = true,
                lines = listOf("011 - Rota Customizada Cidadão", "024 - Circular Centro")
            )
        )
    }
}