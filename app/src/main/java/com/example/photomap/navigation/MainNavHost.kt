package com.example.photomap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.home.navigation.homeNavGraph
import com.example.splash.navigation.splashNavGraph

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    mainNavigator: MainNavigator
) {
    NavHost(
        navController = mainNavigator.navController,
        startDestination = mainNavigator.startDestination
    ) {

        splashNavGraph(navigateHome = { mainNavigator.navigateHome() })
        homeNavGraph()
    }
}