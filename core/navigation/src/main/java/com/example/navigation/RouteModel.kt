package com.example.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Main : Route
    @Serializable data object Map : Route
    @Serializable data object EditFlow : Route
}

sealed interface MainTabRoute : Route {
    @Serializable data object Home : MainTabRoute
    @Serializable data object Search : MainTabRoute
}

sealed interface EditRoute : Route {
    @Serializable data class Edit(val photoId: Long? = null) : EditRoute
    @Serializable data object SelectLocation : EditRoute
}