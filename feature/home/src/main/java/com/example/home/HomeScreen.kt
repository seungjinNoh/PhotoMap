package com.example.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Composable
fun HomeScreen(
    onEditClick: () -> Unit
) {

    Text("Home Screen")
    Button(onClick = onEditClick) {
        Text("Edit 화면으로 이동")
    }
}