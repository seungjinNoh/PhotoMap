package com.example.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.splash.SplashRoute
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun NavController.navigateSplash(navOptions: NavOptions) {
    navigate(SplashRoute, navOptions)
}

fun NavGraphBuilder.splashNavGraph(
    navigateHome: () -> Unit
) {
    composable<SplashRoute> {
        SplashRoute(navigateHome = navigateHome)
    }
}