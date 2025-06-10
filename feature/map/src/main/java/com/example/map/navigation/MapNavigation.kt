package com.example.map.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.map.MapScreen
import com.example.model.photo.PhotoInfo
import com.example.navigation.MainTabRoute

fun NavController.navigateMap() {
    navigate(MainTabRoute.Map)
}

fun NavGraphBuilder.mapNavGraph(
    onEditClick: (PhotoInfo) -> Unit,
    padding: PaddingValues
) {
    composable<MainTabRoute.Map> {
        MapScreen(
            onEditClick = onEditClick,
            padding = padding
        )
    }
}