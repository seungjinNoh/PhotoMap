package com.example.photomap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.navigation.EditRoute
import com.example.navigation.MainTabRoute
import com.example.navigation.Route
import com.example.photomap.MainTab

class MainNavigator(
    val navController: NavHostController
) {

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            launchSingleTop = true
            restoreState = true
            popUpTo<Route.Main> { saveState = true }
        }

        when (tab) {
            MainTab.HOME -> navigateHome(navOptions)
            MainTab.SEARCH -> navigateSearch(navOptions)
        }
    }

    fun navigateHome(navOptions: NavOptions) {
        navController.navigate(MainTabRoute.Home, navOptions)
    }

    fun navigateSearch(navOptions: NavOptions) {
        navController.navigate(MainTabRoute.Search, navOptions)
    }

    fun navigateEdit(photoId: Long?) {
        navController.navigate(EditRoute.Edit(photoId))
    }

    fun navigateMap() {
        navController.navigate(Route.Map)
    }

    fun navigateEditAddMode() {
        navController.navigate(EditRoute.Edit(null))
    }

    fun navigateSelectLocation() {
        navController.navigate(EditRoute.SelectLocation)
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
) : MainNavigator = remember(navController) {
    MainNavigator(navController)
}

