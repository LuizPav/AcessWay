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
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.accessway.R
import com.example.accessway.viewmodels.HomeViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

@Composable
fun MapBase(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {

    val camPosState = rememberCameraPositionState()
    val context = LocalContext.current

    var busStopIcon by remember {
        mutableStateOf<BitmapDescriptor?>(null)
    }

    LaunchedEffect(context) {
        MapsInitializer.initialize(context)
        busStopIcon = bitmapDescriptorFromVector(context, R.drawable.ic_bus_stop)
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
            // Auto select the registered stop to trigger the retractable bottom sheet
            viewModel.selectedStop = viewModel.stops.lastOrNull()
        }

    ) {

        viewModel.stops.forEach { stop ->

            stop.location?.let { location ->

                Marker(
                    state = MarkerState(position = location),
                    title = stop.name,
                    icon = if (stop.isBusStop) busStopIcon else null,
                    onClick = {
                        viewModel.selectedStop = stop
                        true // consume click to suppress default info window
                    }
                )
            }
        }
    }
}