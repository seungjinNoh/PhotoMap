package com.example.edit.navigation

import android.annotation.SuppressLint
import android.net.Uri
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
import com.example.model.photo.PhotoUiModel
import com.example.navigation.Route
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NavController.navigateEdit(photoUiModel: PhotoUiModel) {
    val photoUiModelJson = Uri.encode(Json.encodeToString(photoUiModel))
    navigate("edit_screen?photoUiModel=$photoUiModelJson")
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
        startDestination = "edit_screen?photoUiModel={photoUiModel}"
    ) {
        composable(
            route = "edit_screen?photoUiModel={photoUiModel}",
            arguments = listOf(
                navArgument("photoUiModel") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { navBackStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry("edit_root") }
            val viewModel: EditViewModel = hiltViewModel(parentEntry)
            val photoUiModelJson = navBackStackEntry.arguments?.getString("photoUiModel")
            val photoUiModel = photoUiModelJson?.let { Json.decodeFromString<PhotoUiModel>(it) }

            EditScreen(
                photoUiModel = photoUiModel,
                onBackClick = onBackClick,
                onSelectLocationClick =  onSelectLocationClick,
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