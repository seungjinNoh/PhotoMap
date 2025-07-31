package com.example.map.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.map.MapScreen
import com.example.navigation.MainTabRoute

fun NavController.navigateMap() {
    navigate(MainTabRoute.Map)
}

fun NavGraphBuilder.mapNavGraph(
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    padding: PaddingValues
) {
    composable<MainTabRoute.Map> {
        MapScreen(
            onBackClick = onBackClick,
            onEditClick = onEditClick,
            padding = padding
        )
    }
}