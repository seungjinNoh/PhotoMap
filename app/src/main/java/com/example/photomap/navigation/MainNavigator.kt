package com.example.photomap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.home.navigation.navigateHome
import com.example.map.navigation.navigateMap
import com.example.navigation.Route
import com.example.photomap.MainTab

internal class MainNavigator(
    val navController: NavHostController
) {

    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Splash

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            // todo 각각의 navigate로 바꾸기
            MainTab.HOME -> navController.navigateHome(navOptions)
            MainTab.MAP -> navController.navigateMap(navOptions)
            MainTab.SEARCH -> navController.navigateHome(navOptions)
        }
    }

    fun navigateHome() {
        val navOption = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
        navController.navigateHome(navOption)
    }

    @Composable
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
) : MainNavigator = remember(navController) {
    MainNavigator(navController)
}

