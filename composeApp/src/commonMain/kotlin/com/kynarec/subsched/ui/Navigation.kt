package com.kynarec.subsched.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kynarec.subsched.ui.screens.MainScreen

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
                    MainScreen(navController)
                }
            }
        }

        composable<NavRoutes.SettingsScreen> {
            ScreenWithContent(navController){
                Text("SettingsScreen")
            }
        }
    }
}