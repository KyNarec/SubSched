package com.kynarec.subsched.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun ScreenWithContent(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSettingsScreen = remember(currentRoute) {
        currentRoute?.startsWith(NavRoutes.SettingsScreen::class.qualifiedName!!) == true
    }

    val isHomeScreen = remember(currentRoute) {
        currentRoute?.startsWith(NavRoutes.HomeScreen::class.qualifiedName!!) == true
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = isHomeScreen,
                    onClick = { if (!isHomeScreen) navController.navigate(NavRoutes.HomeScreen) },
                    icon = { Icon(
                        Icons.Default.Home,
                        contentDescription = "Home"
                    ) },
                    label = {
                        Text(
                            "Home",
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }
                )

                NavigationBarItem(
                    selected = isSettingsScreen,
                    onClick = { if (!isSettingsScreen) navController.navigate(NavRoutes.SettingsScreen) },
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
    )
    { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            content
        }
    }
}