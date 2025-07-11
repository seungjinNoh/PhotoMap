package com.example.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Edit : Route

    @Serializable
    data class SelectLocation(val latitude: Double, val longitude: Double) : Route

}

sealed interface MainTabRoute : Route {

    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object Map : MainTabRoute

    @Serializable
    data object Search : MainTabRoute
}
