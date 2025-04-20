package com.example.edit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.edit.EditScreen
import com.example.edit.SelectLocationScreen
import com.example.navigation.Route

fun NavController.navigateEdit() {
    navigate(Route.Edit)
}

fun NavController.navigateSelectLocation() {
    navigate(Route.SelectLocation)
}

fun NavGraphBuilder.editNavGraph(
    onBackClick: () -> Unit
) {

    composable<Route.Edit> {
        EditScreen(
            onBackClick = onBackClick
        )
    }

    composable<Route.SelectLocation> {
        SelectLocationScreen(
            onBackClick = onBackClick
        )
    }
}