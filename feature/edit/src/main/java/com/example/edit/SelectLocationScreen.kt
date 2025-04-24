package com.example.edit

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun SelectLocationScreen(
    latitude: Double,
    longitude: Double,
    onBackClick: () -> Unit
) {
    Text("Select Location Screen: $latitude / $longitude")
}