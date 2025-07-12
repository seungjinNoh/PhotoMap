package com.example.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.navigation.MainTabRoute
import com.example.search.SearchScreen

fun NavController.navigateSearch(navOptions: NavOptions) {
    navigate(MainTabRoute.Search, navOptions)
}

fun NavGraphBuilder.searchNavGraph(
    onEditClick: (Long) -> Unit,
    padding: PaddingValues
) {
    composable<MainTabRoute.Search> {
        SearchScreen(
            onEditClick = onEditClick,
            padding = padding
        )
    }
}