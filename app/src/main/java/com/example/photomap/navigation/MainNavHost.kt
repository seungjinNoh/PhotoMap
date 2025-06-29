package com.example.photomap.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.edit.navigation.editNavGraph
import com.example.home.navigation.homeNavGraph
import com.example.map.navigation.mapNavGraph
import com.example.search.navigation.searchNavGraph
import com.example.splash.navigation.splashNavGraph

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    mainNavigator: MainNavigator,
    padding: PaddingValues
) {

    NavHost(
        navController = mainNavigator.navController,
        startDestination = mainNavigator.startDestination
    ) {

        splashNavGraph(navigateHome = { mainNavigator.navigateHome() })
        homeNavGraph(
            onEditClick = { mainNavigator.navigateEdit(it) },
            onAddClick = { mainNavigator.navigateEditAddMode() },
            onMapClick = { mainNavigator.navigateMap() },
            padding = padding
        )
        mapNavGraph(
            onEditClick = { mainNavigator.navigateEdit(it) },
            padding = padding
        )
        editNavGraph(
            onBackClick = mainNavigator::popBackStack,
            onSelectLocationClick = { mainNavigator.navigateSelectLocation(it.latitude, it.longitude) },
            navController = mainNavigator.navController
        )
        searchNavGraph(
            onEditClick = { mainNavigator.navigateEdit(it) },
            padding = padding
        )
    }
}