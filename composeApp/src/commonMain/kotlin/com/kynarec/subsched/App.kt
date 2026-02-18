package com.kynarec.subsched

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kynarec.subsched.ui.navigation.NavRoutes
import com.kynarec.subsched.ui.navigation.Navigation
import com.kynarec.subsched.ui.screens.home.misc.WindowInfo
import com.kynarec.subsched.ui.screens.home.misc.rememberWindowInfo
import com.kynarec.subsched.ui.theme.SubSchedTheme
import com.kynarec.subsched.util.WindowHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    windowHandler: WindowHandler? = null
) {
    val viewModel: SubSchedViewModel = koinViewModel()
    viewModel.darkThemeDefault = isSystemInDarkTheme()

    val darkTheme by viewModel.darkThemeFlow.collectAsStateWithLifecycle(viewModel.darkThemeDefault)

    viewModel.isFirstLaunch = false
    SubSchedTheme(
        darkTheme = darkTheme
    ) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val isSettingsScreen = remember(navBackStackEntry) {
            navBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute<NavRoutes.SettingsGraph>()
            } == true
        }

        val isHomeScreen = remember(currentRoute) {
            currentRoute?.startsWith(NavRoutes.HomeScreen::class.qualifiedName!!) == true
        }
        val isTomorrowScreen = remember(currentRoute) {
            currentRoute?.startsWith(NavRoutes.TomorrowScreen::class.qualifiedName!!) == true
        }
        val isNewsScreen = remember(currentRoute) {
            currentRoute?.startsWith(NavRoutes.NewsScreen::class.qualifiedName!!) == true
        }
        val windowInfo = rememberWindowInfo()
        val snackBarHostState = remember { SnackbarHostState() }
        Scaffold(
            bottomBar = {
                if (windowInfo.screenWidthInfo != WindowInfo.WindowType.Expanded) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = isHomeScreen,
                            onClick = { if (!isHomeScreen) navController.navigate(NavRoutes.HomeScreen) },
                            icon = {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = "Home"
                                )
                            },
                            label = {
                                Text(
                                    "Home",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp
                                )
                            }
                        )

                        NavigationBarItem(
                            selected = isTomorrowScreen,
                            onClick = { if (!isTomorrowScreen) navController.navigate(NavRoutes.TomorrowScreen) },
                            icon = {
                                Icon(
                                    Icons.Default.Today,
                                    contentDescription = "Tomorrow"
                                )
                            },
                            label = {
                                Text(
                                    "Tomorrow",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp
                                )
                            }
                        )

                        NavigationBarItem(
                            selected = isNewsScreen,
                            onClick = { if (!isNewsScreen) navController.navigate(NavRoutes.NewsScreen) },
                            icon = {
                                Icon(
                                    Icons.Default.Newspaper,
                                    contentDescription = "News"
                                )
                            },
                            label = {
                                Text(
                                    "News",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp
                                )
                            }
                        )

                        NavigationBarItem(
                            selected = isSettingsScreen,
                            onClick = { if (!isSettingsScreen) navController.navigate(NavRoutes.Settings.Root) },
                            icon = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Settings"
                                )
                            },
                            label = {
                                Text(
                                    "Settings",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp
                                )
                            }
                        )
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) { contentPadding ->
            Box(Modifier.fillMaxSize().padding(contentPadding))
            {
                Navigation(navController, windowInfo = windowInfo, windowHandler = windowHandler, snackBarHostState = snackBarHostState)
            }
        }
    }
}