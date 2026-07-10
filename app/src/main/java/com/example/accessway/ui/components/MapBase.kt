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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    tintColor: Int? = null
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    val wrapped = androidx.core.graphics.drawable.DrawableCompat.wrap(drawable).mutate()
    if (tintColor != null) {
        androidx.core.graphics.drawable.DrawableCompat.setTint(wrapped, tintColor)
    }
    wrapped.setBounds(0, 0, wrapped.intrinsicWidth, wrapped.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        wrapped.intrinsicWidth,
        wrapped.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    wrapped.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

@Composable
fun MapBase(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {

    val camPosState = rememberCameraPositionState()
    val context = LocalContext.current

    var showNamingDialog by remember { mutableStateOf(false) }
    var clickedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var inputStopName by remember { mutableStateOf("") }

    var busStopIcon by remember {
        mutableStateOf<BitmapDescriptor?>(null)
    }
    var favoriteBusStopIcon by remember {
        mutableStateOf<BitmapDescriptor?>(null)
    }

    LaunchedEffect(context) {
        MapsInitializer.initialize(context)
        busStopIcon = bitmapDescriptorFromVector(context, R.drawable.ic_bus_stop)
        favoriteBusStopIcon = bitmapDescriptorFromVector(context, R.drawable.ic_bus_stop_favorite)
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
                    if (location != null) {
                        camPosState.move(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                    LatLng(location.latitude, location.longitude),
                                    15f
                                )
                            )
                        )
                    } else {
                        try {
                            val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
                                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                                1000
                            ).setMaxUpdates(1).build()

                            fusedLocationClient.requestLocationUpdates(
                                locationRequest,
                                object : com.google.android.gms.location.LocationCallback() {
                                    override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                                        result.lastLocation?.let { freshLoc ->
                                            camPosState.move(
                                                CameraUpdateFactory.newCameraPosition(
                                                    CameraPosition.fromLatLngZoom(
                                                        LatLng(freshLoc.latitude, freshLoc.longitude),
                                                        15f
                                                    )
                                                )
                                            )
                                        }
                                    }
                                },
                                android.os.Looper.getMainLooper()
                            )
                        } catch (e: SecurityException) {
                            // Permission might have been revoked
                        }
                    }
                }
        }
    }

    LaunchedEffect(camPosState.isMoving) {
        if (!camPosState.isMoving) {
            val center = camPosState.position.target
            if (center.latitude != 0.0 && center.longitude != 0.0) {
                viewModel.loadStopsFromApi(center.latitude, center.longitude)
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
            clickedLatLng = latLng
            inputStopName = ""
            showNamingDialog = true
        }

    ) {

        val allStops = (viewModel.stops + viewModel.favoriteStops).distinctBy { it.id.ifEmpty { it.name } }
        allStops.forEach { stop ->

            stop.location?.let { location ->

                val isFav = viewModel.isFavorite(stop)
                Marker(
                    state = MarkerState(position = location),
                    title = stop.name,
                    icon = if (stop.isBusStop) {
                        if (isFav) favoriteBusStopIcon else busStopIcon
                    } else null,
                    onClick = {
                        viewModel.selectedStop = stop
                        true // consume click to suppress default info window
                    }
                )
            }
        }
    }

    if (showNamingDialog) {
        AlertDialog(
            onDismissRequest = { showNamingDialog = false },
            title = { Text("Nome da Parada") },
            text = {
                Column {
                    Text("Informe o nome da nova parada:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputStopName,
                        onValueChange = { inputStopName = it },
                        placeholder = { Text("Ex: Parada da Avenida") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val latLng = clickedLatLng
                        if (latLng != null && inputStopName.isNotBlank()) {
                            viewModel.registerPoint(inputStopName, latLng)
                            viewModel.selectedStop = viewModel.stops.lastOrNull()
                        }
                        showNamingDialog = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNamingDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}