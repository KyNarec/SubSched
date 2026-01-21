package com.kynarec.subsched.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kynarec.subsched.ui.ScreenWithContent
import com.kynarec.subsched.ui.screens.home.HomeScreen
import com.kynarec.subsched.ui.screens.home.MultiPaneHomeLayout
import com.kynarec.subsched.ui.screens.home.misc.WindowInfo
import com.kynarec.subsched.ui.screens.home.misc.rememberWindowInfo
import com.kynarec.subsched.ui.screens.news.NewsScreen
import com.kynarec.subsched.ui.screens.settings.Account
import com.kynarec.subsched.ui.screens.settings.Appearance
import com.kynarec.subsched.ui.screens.settings.Root
import com.kynarec.subsched.ui.screens.settings.multipane.MultiPaneRootLayout
import com.kynarec.subsched.ui.screens.tomorrow.TomorrowScreen

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
            composable<NavRoutes.HomeScreen> (
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                }
            ){
                LaunchedEffect(windowInfo) {
                    println("Width: ${windowInfo.screenWidth}")
                    println("Height: ${windowInfo.screenHeight}")
                }
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneHomeLayout(navController = navController)
                } else {
                    ScreenWithContent(navController) {
                        HomeScreen(navController = navController)
                    }
                }
            }

            composable<NavRoutes.TomorrowScreen> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneHomeLayout(navController = navController)
                } else {
                    ScreenWithContent(navController) {
                        TomorrowScreen()
                    }
                }
            }

            composable<NavRoutes.NewsScreen> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneHomeLayout(navController = navController)
                } else {
                    ScreenWithContent(navController) {
                        NewsScreen()
                    }
                }
            }

        }

        navigation<NavRoutes.SettingsGraph>(
            startDestination = NavRoutes.Settings.Root
        ) {
            composable<NavRoutes.Settings.Root>(
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                }
            ) {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneRootLayout(navController = navController)
                } else {
                    ScreenWithContent(navController) {
                        Root(navController = navController)
                    }
                }
            }

            composable<NavRoutes.Settings.Account>(
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                }
            ) {
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

            composable<NavRoutes.Settings.Appearance>(
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) +
                            fadeIn(animationSpec = tween(350))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) +
                            fadeOut(animationSpec = tween(350))
                }
            )
            {
                Appearance(
                    navController = navController
                )
            }
        }
    }
}