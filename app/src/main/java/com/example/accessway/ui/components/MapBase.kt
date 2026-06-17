package com.example.accessway.ui.components

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.accessway.viewmodels.HomeViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapBase(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {

    val camPosState = rememberCameraPositionState()
    val context = LocalContext.current

    var openedCard by remember {
        mutableStateOf(false)
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

            if (openedCard) {

                openedCard = false

            } else {

                viewModel.registerPoint(
                    latLng
                )

                openedCard = true
            }
        }

    ) {

        viewModel.stops.forEach { stop ->

            stop.location?.let { location ->

                MarkerInfoWindowContent(

                    state = MarkerState(
                        position = location
                    ),

                    title = stop.name,

                    onClick = {

                        openedCard = true

                        false
                    }

                ) {

                    Column(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(
                                horizontal = 20.dp,
                                vertical = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "📍 ${stop.name}"
                        )

                        Text(
                            text = "⭐ ${stop.avaliation} avaliações",
                            modifier = Modifier.padding(top = 6.dp),
                            color = Color.Gray
                        )

                        Text(
                            text = "♿ Acessibilidade",
                            modifier = Modifier.padding(top = 2.dp),
                            color = Color.Gray
                        )

                        Button(

                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),

                            shape = RoundedCornerShape(16.dp),

                            onClick = {

                                println("Abrir avaliações")

                            }

                        ) {

                            Text("Ver avaliações")
                        }

                        OutlinedButton(

                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),

                            shape = RoundedCornerShape(16.dp),

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
}