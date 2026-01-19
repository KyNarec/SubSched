package com.kynarec.subsched.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kynarec.subsched.ui.ScreenWithContent
import com.kynarec.subsched.ui.screens.home.HomeScreen
import com.kynarec.subsched.ui.screens.home.MultiPaneHomeLayout
import com.kynarec.subsched.ui.screens.home.misc.WindowInfo
import com.kynarec.subsched.ui.screens.home.misc.rememberWindowInfo
import com.kynarec.subsched.ui.screens.settings.Account
import com.kynarec.subsched.ui.screens.settings.Appearance
import com.kynarec.subsched.ui.screens.settings.Root
import com.kynarec.subsched.ui.screens.settings.multipane.MultiPaneRootLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController,
) {
    val windowInfo = rememberWindowInfo()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.MainGraph
    ) {
        navigation<NavRoutes.MainGraph>(
            startDestination = NavRoutes.HomeScreen
        ) {
            composable<NavRoutes.HomeScreen> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneHomeLayout(navController = navController)
                } else {
                    ScreenWithContent(navController) {
                        HomeScreen(navController = navController)
                    }
                }
            }
        }

        navigation<NavRoutes.SettingsGraph>(
            startDestination = NavRoutes.Settings.Root
        ) {
            composable<NavRoutes.Settings.Root> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneRootLayout(navController = navController)
                } else {
                    ScreenWithContent(navController) {
                        Root(navController = navController)
                    }
                }
            }

            composable<NavRoutes.Settings.Account> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    Row(
                    ) {
                        MultiPaneRootLayout(navController = navController)
                        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Account(
                            navController = navController
                        )
                    }
                } else {
                    Account(
                        navController = navController
                    )
                }

            }

            composable<NavRoutes.Settings.Appearance> {
                Appearance(
                    navController = navController
                )
            }
        }
    }
}