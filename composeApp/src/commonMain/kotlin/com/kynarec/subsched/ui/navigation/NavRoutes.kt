package com.kynarec.subsched.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {

    @Serializable
    data object MainGraph : NavRoutes()

    @Serializable
    data object HomeScreen : NavRoutes()

    @Serializable
    data object TomorrowScreen : NavRoutes()

    @Serializable
    data object NewsScreen : NavRoutes()

    @Serializable
    sealed class Settings {
        @Serializable
        data object Root : Settings()

        @Serializable
        data object Account : Settings()

        @Serializable
        data object Appearance : Settings()
    }

    @Serializable
    data object SettingsGraph : NavRoutes()

}