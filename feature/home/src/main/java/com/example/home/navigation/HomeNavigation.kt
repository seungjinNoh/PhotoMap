package com.example.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateHome() {
    navigate(HomeRoute) {
        popUpTo(id = 0) { inclusive = true }
    }
}

fun NavGraphBuilder.homeNavGraph() {
    composable<HomeRoute> {
        HomeRoute()
    }
}