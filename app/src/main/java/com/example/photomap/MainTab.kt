package com.example.photomap

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.navigation.MainTabRoute
import com.example.navigation.Route

internal enum class MainTab(
    val imageVector: ImageVector,
    internal val contentDescription: String,
    val route: MainTabRoute
) {

    HOME(
        imageVector = Icons.Default.Home,
        contentDescription = "홈",
        route = MainTabRoute.Home
    ),
    SEARCH(
        imageVector = Icons.Default.Search,
        contentDescription = "검색",
        route = MainTabRoute.Search
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }

    }


}