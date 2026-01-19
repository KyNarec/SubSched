package com.kynarec.subsched.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kynarec.subsched.ui.ScreenWithContent
import com.kynarec.subsched.ui.screens.home.HomeScreen
import com.kynarec.subsched.ui.screens.settings.Account
import com.kynarec.subsched.ui.screens.settings.Appearance
import com.kynarec.subsched.ui.screens.settings.Root

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.MainGraph
    ) {
        navigation<NavRoutes.MainGraph>(
            startDestination = NavRoutes.HomeScreen
        ) {
            composable<NavRoutes.HomeScreen> {
                ScreenWithContent(navController) {
                    HomeScreen(navController = navController)
                }
            }
        }

        navigation<NavRoutes.SettingsGraph>(
            startDestination = NavRoutes.Settings.Root
        ) {
            composable<NavRoutes.Settings.Root> {
                ScreenWithContent(navController) {
                    Root(
                        navController = navController
                    )
                }
            }

            composable<NavRoutes.Settings.Account> {
                Account(
                    navController = navController
                )
            }

            composable<NavRoutes.Settings.Appearance> {
                Appearance(
                    navController = navController
                )
            }
        }
    }
}