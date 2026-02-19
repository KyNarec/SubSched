package com.kynarec.subsched.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kynarec.subsched.SubSchedViewModel
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
import com.kynarec.subsched.util.WindowHandler
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController,
    windowInfo: WindowInfo = rememberWindowInfo(),
    snackBarHostState: SnackbarHostState,
    viewModel: SubSchedViewModel = koinViewModel(),
    windowHandler: WindowHandler? = null
) {
    val transitionEffect by viewModel.transitionEffectFlow.collectAsStateWithLifecycle(viewModel.transitionEffect)

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MainGraph,
        enterTransition = {
            when (transitionEffect) {
                TransitionEffect.None -> EnterTransition.None
                TransitionEffect.Expand -> expandIn(
                    animationSpec = tween(
                        350,
                        easing = LinearOutSlowInEasing
                    ), expandFrom = Alignment.TopStart
                )

                TransitionEffect.Fade -> fadeIn(animationSpec = tween(350))
                TransitionEffect.Scale -> scaleIn(animationSpec = tween(350))
                TransitionEffect.SlideVertical -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up
                )

                TransitionEffect.SlideHorizontal -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            }
        },
        exitTransition = {
            when (transitionEffect) {
                TransitionEffect.None -> ExitTransition.None
                TransitionEffect.Expand -> shrinkOut(
                    animationSpec = tween(
                        350,
                        easing = FastOutSlowInEasing
                    ), shrinkTowards = Alignment.TopStart
                )

                TransitionEffect.Fade -> fadeOut(animationSpec = tween(350))
                TransitionEffect.Scale -> scaleOut(animationSpec = tween(350))
                TransitionEffect.SlideVertical -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down
                )

                TransitionEffect.SlideHorizontal -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        },
        popEnterTransition = {
            when (transitionEffect) {
                TransitionEffect.None -> EnterTransition.None
                TransitionEffect.Expand -> expandIn(
                    animationSpec = tween(
                        350,
                        easing = LinearOutSlowInEasing
                    ), expandFrom = Alignment.TopStart
                )

                TransitionEffect.Fade -> fadeIn(animationSpec = tween(350))
                TransitionEffect.Scale -> scaleIn(animationSpec = tween(350))
                TransitionEffect.SlideVertical -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up
                )

                TransitionEffect.SlideHorizontal -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            }
        },
        popExitTransition = {
            when (transitionEffect) {
                TransitionEffect.None -> ExitTransition.None
                TransitionEffect.Expand -> shrinkOut(
                    animationSpec = tween(
                        350,
                        easing = FastOutSlowInEasing
                    ), shrinkTowards = Alignment.TopStart
                )

                TransitionEffect.Fade -> fadeOut(animationSpec = tween(350))
                TransitionEffect.Scale -> scaleOut(animationSpec = tween(350))
                TransitionEffect.SlideVertical -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down
                )

                TransitionEffect.SlideHorizontal -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        }
    ) {
        navigation<NavRoutes.MainGraph>(
            startDestination = NavRoutes.HomeScreen
        ) {
            composable<NavRoutes.HomeScreen> {
                LaunchedEffect(windowInfo) {
                    println("Width: ${windowInfo.screenWidth}")
                    println("Height: ${windowInfo.screenHeight}")
                }
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneHomeLayout(navController = navController, snackBarHostState = snackBarHostState)
                } else {
                    ScreenWithContent(navController) {
                        HomeScreen(snackBarHostState = snackBarHostState)
                    }
                }
            }

            composable<NavRoutes.TomorrowScreen> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneHomeLayout(navController = navController, snackBarHostState = snackBarHostState)
                } else {
                    ScreenWithContent(navController) {
                        TomorrowScreen(snackBarHostState = snackBarHostState)
                    }
                }
            }

            composable<NavRoutes.NewsScreen> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneHomeLayout(navController = navController, snackBarHostState = snackBarHostState)
                } else {
                    ScreenWithContent(navController) {
                        NewsScreen(snackBarHostState = snackBarHostState)
                    }
                }
            }

        }

        navigation<NavRoutes.SettingsGraph>(
            startDestination = NavRoutes.Settings.Root
        ) {
            composable<NavRoutes.Settings.Root> {
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    MultiPaneRootLayout(navController = navController, windowHandler = windowHandler)
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
                        MultiPaneRootLayout(navController = navController, windowHandler = windowHandler)
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
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    Row(
                    ) {
                        MultiPaneRootLayout(navController = navController, windowHandler = windowHandler)
                        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Appearance(
                            navController = navController,
                            windowHandler = windowHandler
                        )
                    }
                } else {
                    Appearance(
                        navController = navController,
                        windowHandler = windowHandler
                    )
                }
            }
        }
    }
}