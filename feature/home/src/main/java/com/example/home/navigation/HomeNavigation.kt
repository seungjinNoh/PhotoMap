package com.example.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.home.HomeScreen
import com.example.model.photo.PhotoInfo
import com.example.navigation.MainTabRoute

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onEditClick: (PhotoInfo) -> Unit,
    onAddClick: () -> Unit,
    padding: PaddingValues
) {
    composable<MainTabRoute.Home> {
        HomeScreen(
            onEditClick = onEditClick,
            onAddClick = onAddClick,
            padding = padding
        )
    }
}