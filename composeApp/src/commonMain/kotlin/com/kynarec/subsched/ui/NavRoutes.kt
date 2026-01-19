package com.kynarec.subsched.ui

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {

    @Serializable
    data object MainGraph : NavRoutes()

    @Serializable
    data object HomeScreen : NavRoutes()

    @Serializable
    data object SettingsScreen: NavRoutes()
}