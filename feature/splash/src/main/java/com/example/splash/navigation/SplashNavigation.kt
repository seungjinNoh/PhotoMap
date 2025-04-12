package com.example.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.navigation.Route
import com.example.splash.SplashRoute


fun NavController.navigateSplash(navOptions: NavOptions) {
    navigate(Route.Splash, navOptions)
}

fun NavGraphBuilder.splashNavGraph(
    navigateHome: () -> Unit
) {
    composable<Route.Splash> {
        SplashRoute(navigateHome = navigateHome)
    }
}
