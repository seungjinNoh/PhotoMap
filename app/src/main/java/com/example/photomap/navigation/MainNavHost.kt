package com.example.photomap.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.edit.navigation.editNavGraph
import com.example.home.navigation.homeNavGraph
import com.example.map.navigation.mapNavGraph
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
            onEditClick = { mainNavigator.navigateEdit() }
        )
        mapNavGraph()
        editNavGraph(
            onBackClick = mainNavigator::popBackStack,
            onSelectLocationClick = { mainNavigator.navigateSelectLocation(it.latitude, it.longitude) }
        )
    }
}