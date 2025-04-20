package com.example.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.home.HomeScreen
import com.example.navigation.MainTabRoute

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onEditClick: () -> Unit
) {
    composable<MainTabRoute.Home> {
        HomeScreen(
            onEditClick = onEditClick
        )
    }
}