package com.example.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.designsystem.theme.PhotoMapTheme
import com.example.edit.model.EditUiState
import com.example.utils.location.LocationProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun SelectLocationScreen(
    onBackClick: () -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val locationProvider = remember { LocationProvider(context) }
    val cameraPositionState = rememberCameraPositionState()

    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var w3wAddress by remember { mutableStateOf<String?>(null) }

    val defaultLatLng = LatLng(37.5665, 126.9780)

    LaunchedEffect(uiState) {
        val photo = (uiState as? EditUiState.Success)?.photoUiModel
        val targetLatLng = when {
            photo?.latitude != null && photo.longitude != null -> {
                val latLng = LatLng(photo.latitude!!, photo.longitude!!)
                selectedLatLng = latLng
                w3wAddress = photo.w3w
                latLng
            }
            else -> {
                val location = locationProvider.getCurrentLocation()
                location?.let { LatLng(it.latitude, it.longitude) } ?: defaultLatLng
            }
        }
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(targetLatLng, 15f))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false
                ),
                onMapClick = { latLng ->
                    selectedLatLng = latLng
                    viewModel.viewModelScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        )
                        viewModel.getW3WUseCase(latLng.latitude, latLng.longitude).collect { response ->
                            w3wAddress = response.words
                        }
                    }
                }
            ) {
                selectedLatLng?.let {
                    val currentLatitude = selectedLatLng?.latitude
                    val currentLongitude = selectedLatLng?.longitude
                    val latLngSnippet = if (currentLatitude != null && currentLongitude != null) {
                        String.format("%.4f / %.4f", currentLatitude, currentLongitude)
                    } else {
                        ""
                    }
                    Marker(
                        state = MarkerState(position = it),
                        title = w3wAddress,
                        snippet = latLngSnippet,
                        onClick = {
                            false
                        }
                    )
                }
            }

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = Color.Black
                )
            }

            Button(
                onClick = {
                    val latLng = selectedLatLng
                    val w3w = w3wAddress
                    if (latLng != null && w3w != null) {
                        viewModel.updateLocation(latLng.latitude, latLng.longitude, w3w)
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PhotoMapTheme.colors.button
                )
            ) {
                Text("선택한 위치 저장", color = PhotoMapTheme.colors.textTitle)

            }
        }
    }
}