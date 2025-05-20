package com.example.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.edit.model.EditUiState
import com.example.utils.location.LocationProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
internal fun SelectLocationScreen(
    latitude: Double,
    longitude: Double,
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

    // 1. 초기 카메라 이동 처리
    LaunchedEffect(uiState) {
        val photo = (uiState as? EditUiState.Success)?.photoInfo
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
                            false // 기본 동작(정보창 표시)을 그대로 유지
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val latLng = selectedLatLng
                val w3w = w3wAddress
                if (latLng != null && w3w != null) {
                    viewModel.updateLocation(latLng.latitude, latLng.longitude, w3w)
                    // 실제 w3w는 viewModel.updateLocation 내부에서 처리됨
                    onBackClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("선택한 위치 저장")
        }
    }
}