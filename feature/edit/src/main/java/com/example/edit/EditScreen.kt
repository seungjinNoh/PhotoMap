package com.example.edit

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.navigation.Route

@Composable
internal fun EditScreen(
    onBackClick: () -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit
) {
    Text("Edit Screen")

    Button(
        onClick = { onSelectLocationClick(Route.SelectLocation(
            latitude = 123.123, longitude = 456.456
        )) }
    ) {
        Text("selection Button")
    }

}