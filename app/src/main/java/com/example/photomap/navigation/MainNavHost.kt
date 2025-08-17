package com.example.photomap.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.designsystem.theme.PhotoMapTheme
import com.example.edit.EditScreen
import com.example.edit.EditViewModel
import com.example.edit.SelectLocationScreen
import com.example.home.HomeScreen
import com.example.map.MapScreen
import com.example.navigation.EditRoute
import com.example.navigation.MainTabRoute
import com.example.navigation.Route
import com.example.photomap.MainBottomBar
import com.example.photomap.MainTab
import com.example.search.SearchScreen
import com.example.splash.SplashScreen
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainNavHost(
    mainNavigator: MainNavigator
) {

    NavHost(
        navController = mainNavigator.navController,
        startDestination = Route.Splash
    ) {

        composable<Route.Splash> {
            SplashScreen(
                navigateHome = {
                    mainNavigator.navController.navigate(Route.Main) {
                        popUpTo<Route.Splash> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        navigation<Route.Main>(startDestination = MainTabRoute.Home) {
            composable<MainTabRoute.Home> {
                MainScaffold(
                    currentTab = MainTab.HOME,
                    onTabSelected = { mainTab -> mainNavigator.navigate(mainTab) }
                ) { innerPadding ->
                    HomeScreen(
                        onEditClick = { mainNavigator.navigateEdit(it) },
                        onAddClick = { mainNavigator.navigateEditAddMode() },
                        onMapClick = { mainNavigator.navigateMap() },
                        padding = innerPadding
                    )
                }
            }

            composable<MainTabRoute.Search> {
                MainScaffold(
                    currentTab = MainTab.SEARCH,
                    onTabSelected = { mainTab -> mainNavigator.navigate(mainTab) }
                ) { innerPadding ->
                    SearchScreen(
                        onEditClick = { mainNavigator.navigateEdit(it) },
                        padding = innerPadding
                    )
                }
            }
        }

        navigation<Route.EditFlow>(startDestination = EditRoute.Edit()) {
            composable<EditRoute.Edit> { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    mainNavigator.navController.getBackStackEntry(Route.EditFlow)
                }
                val viewModel: EditViewModel = hiltViewModel(parentEntry)

                EditScreen(
                    onBackClick = mainNavigator::popBackStack,
                    onSelectLocationClick = { mainNavigator.navigateSelectLocation() },
                    viewModel = viewModel
                )
            }

            composable<EditRoute.SelectLocation> { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    mainNavigator.navController.getBackStackEntry(Route.EditFlow)
                }
                val viewModel: EditViewModel = hiltViewModel(parentEntry)

                SelectLocationScreen(
                    onBackClick = mainNavigator::popBackStack,
                    viewModel = viewModel
                )
            }
        }

        composable<Route.Map> {
            MapScreen(
                onBackClick = { mainNavigator.navController.popBackStack() },
                onEditClick = { id -> mainNavigator.navController.navigate(EditRoute.Edit(id)) },
            )
        }
    }
}

@Composable
fun MainScaffold(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(PhotoMapTheme.colors.background),
        bottomBar = {
            MainBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .background(PhotoMapTheme.colors.background),
                currentTab = currentTab,
                tabs = MainTab.entries.toPersistentList(),
                onTabSelected = { mainTab -> onTabSelected(mainTab) }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}