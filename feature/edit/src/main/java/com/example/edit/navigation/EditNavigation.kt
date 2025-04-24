package com.example.edit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.edit.EditScreen
import com.example.edit.SelectLocationScreen
import com.example.navigation.Route

fun NavController.navigateEdit() {
    navigate(Route.Edit)
}

fun NavController.navigateSelectLocation(latitude: Double, longitude: Double) {
    navigate(Route.SelectLocation(latitude, longitude))
}

fun NavGraphBuilder.editNavGraph(
    onBackClick: () -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit
) {

    composable<Route.Edit> {
        EditScreen(
            onBackClick = onBackClick,
            onSelectLocationClick = onSelectLocationClick
        )
    }

    composable<Route.SelectLocation> { navBackStackEntry ->
        val latitude = navBackStackEntry.toRoute<Route.SelectLocation>().latitude
        val longitude = navBackStackEntry.toRoute<Route.SelectLocation>().longitude
        SelectLocationScreen(
            latitude = latitude,
            longitude = longitude,
            onBackClick = onBackClick
        )
    }
}