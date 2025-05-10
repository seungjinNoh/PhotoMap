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
import com.example.model.photo.PhotoInfo
import com.example.navigation.Route
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NavController.navigateEdit(photoInfo: PhotoInfo) {
    val photoInfoJson = Uri.encode(Json.encodeToString(photoInfo))
    navigate("edit_screen?photoInfo=$photoInfoJson")
}

fun NavController.navigateEdit() {
    navigate("edit_screen") // photoInfo 없이 → 추가 모드
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
        startDestination = "edit_screen?photoInfo={photoInfo}"
    ) {
        composable(
            route = "edit_screen?photoInfo={photoInfo}",
            arguments = listOf(
                navArgument("photoInfo") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { navBackStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry("edit_root") }
            val viewModel: EditViewModel = hiltViewModel(parentEntry)
            val photoInfoJson = navBackStackEntry.arguments?.getString("photoInfo")
            val photoInfo = photoInfoJson?.let { Json.decodeFromString<PhotoInfo>(it) }

            EditScreen(
                photoInfo = photoInfo,
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