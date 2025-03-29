package com.example.photomap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.home.navigation.navigateHome
import com.example.splash.navigation.SplashRoute

internal class MainNavigator(
    val navController: NavHostController
) {
    val startDestination = SplashRoute

    fun navigateHome() {
        navController.navigateHome()
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
) : MainNavigator = remember(navController) {
    MainNavigator(navController)
}

