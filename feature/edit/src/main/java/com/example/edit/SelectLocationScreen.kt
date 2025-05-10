package com.example.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.edit.model.EditUiState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
internal fun SelectLocationScreen(
    latitude: Double,
    longitude: Double,
    onBackClick: () -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val seoul = LatLng(37.5665, 126.9780)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(seoul, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
    }


    if (uiState is EditUiState.Success) {
        val ui = (uiState as EditUiState.Success)
        Text("위경도: ${ui.photoInfo.latitude} / ${ui.photoInfo.longitude}")
        viewModel.updateTitle("타이틀 수정함")
    }


}