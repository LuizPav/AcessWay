package com.example.accessway.ui.components

import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.accessway.viewmodels.HomeViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerInfoWindowContent

@Composable
fun MapBase(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {

    val camPosState = rememberCameraPositionState()
    val context = LocalContext.current

    var clickedPosition by remember {
        mutableStateOf<LatLng?>(null)
    }

    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    LaunchedEffect(hasLocationPermission) {

        if (hasLocationPermission) {

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    location?.let {

                        camPosState.move(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                    LatLng(
                                        it.latitude,
                                        it.longitude
                                    ),
                                    15f
                                )
                            )
                        )
                    }
                }
        }
    }

    GoogleMap(

        modifier = modifier,

        cameraPositionState = camPosState,

        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        ),

        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true
        ),

        onMapClick = { latLng ->

            clickedPosition = latLng
        }

    ) {

        // marcadores existentes
        viewModel.stops.forEach {

            it.location?.let { location ->

                Marker(
                    state = MarkerState(
                        position = location
                    ),
                    title = it.name
                )
            }
        }

        // marcador criado ao clicar
        clickedPosition?.let { position ->

            MarkerInfoWindowContent(

                state = MarkerState(
                    position = position
                ),

                title = "Parada"

            ) {

                Column(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {

                    Text(
                        text = "📍 Parada selecionada"
                    )

                    Text(
                        text = "⭐ 4.8 (24 avaliações)",
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    Text(
                        text = "♿ Boa acessibilidade",
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    TextButton(
                        modifier = Modifier.padding(top = 8.dp),
                        onClick = {

                            println("Abrir avaliações")

                        }
                    ) {

                        Text("Ver avaliações")
                    }

                    TextButton(

                        onClick = {

                            println("Criar denúncia")

                        }

                    ) {

                        Text("Criar denúncia")
                    }
                }
            }
        }
    }
}