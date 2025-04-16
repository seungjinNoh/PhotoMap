package com.example.map.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.map.MapRoute
import com.example.navigation.MainTabRoute

fun NavController.navigateMap(navOptions: NavOptions) {
    navigate(MainTabRoute.Map, navOptions)
}

fun NavGraphBuilder.mapNavGraph() {
    composable<MainTabRoute.Map> {
        MapRoute()
    }
}