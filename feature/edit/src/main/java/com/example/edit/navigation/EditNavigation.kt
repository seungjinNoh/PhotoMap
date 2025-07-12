package com.example.edit.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.edit.EditScreen
import com.example.edit.EditViewModel
import com.example.edit.SelectLocationScreen
import com.example.navigation.Route

fun NavController.navigateEdit(photoId: Long) {
    navigate("edit_screen?photoId=$photoId")
}

fun NavController.navigateEdit() {
    navigate("edit_screen")
}

fun NavController.navigateSelectLocation(latitude: Double, longitude: Double) {
    navigate(Route.SelectLocation(latitude, longitude))
}

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.editNavGraph(
    onBackClick: () -> Unit,
    onSelectLocationClick: (Route.SelectLocation) -> Unit,
    navController: NavController
) {

    navigation(
        route = "edit_root",
        startDestination = "edit_screen?photoId={photoId}"
    ) {
        composable(
            route = "edit_screen?photoId={photoId}",
            arguments = listOf(
                navArgument("photoId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            val parentEntry = remember { navController.getBackStackEntry("edit_root") }
            val viewModel: EditViewModel = hiltViewModel(parentEntry)

            EditScreen(
                onBackClick = onBackClick,
                onSelectLocationClick = onSelectLocationClick,
                viewModel = viewModel
            )
        }

        composable<Route.SelectLocation> { navBackStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry("edit_root") }
            val viewModel: EditViewModel = hiltViewModel(parentEntry)
            val latitude = navBackStackEntry.toRoute<Route.SelectLocation>().latitude
            val longitude = navBackStackEntry.toRoute<Route.SelectLocation>().longitude
            SelectLocationScreen(
                latitude = latitude,
                longitude = longitude,
                onBackClick = onBackClick,
                viewModel = viewModel
            )
        }
    }
}