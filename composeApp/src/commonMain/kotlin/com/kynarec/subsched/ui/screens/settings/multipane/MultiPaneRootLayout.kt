package com.kynarec.subsched.ui.screens.settings.multipane

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kynarec.subsched.ui.navigation.NavRoutes
import com.kynarec.subsched.ui.screens.settings.Account
import com.kynarec.subsched.ui.screens.settings.Appearance
import com.kynarec.subsched.ui.screens.settings.misc.SettingsFolder
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiPaneRootLayout(
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavRoutes.HomeScreen) }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        val innerNavController = rememberNavController()
        val isShowingSubFolder = remember { mutableStateOf(false) }

        Row(
            Modifier.fillMaxSize()
                .padding(paddingValues = contentPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .animateContentSize()
            ) {
                Column(
                    Modifier.padding(top = 8.dp)
                ) {
                    LazyColumn(
                        Modifier.padding(
                            horizontal = 16.dp
                        )
                    ) {
                        item {
                            ElevatedCard {
                                SettingsFolder(
                                    title = { Text("Account") },
                                    icon = { Icon(Icons.Default.AccountCircle, null) },
                                    onClick = {
                                        if (innerNavController.currentDestination?.route
                                            ?.startsWith(InnerNavRoutes.Account::class.qualifiedName!!) == false)
                                        innerNavController.navigate(InnerNavRoutes.Account)
                                    }
                                )
                            }
                        }
                        item { Spacer(Modifier.height(16.dp)) }
                        item {
                            ElevatedCard {
                                SettingsFolder(
                                    title = { Text("Appearance") },
                                    icon = { Icon(Icons.Rounded.Palette, null) },
                                    onClick = { if (innerNavController.currentDestination?.route
                                        ?.startsWith(InnerNavRoutes.Appearance::class.qualifiedName!!) == false)
                                        innerNavController.navigate(InnerNavRoutes.Appearance)
                                    }
                                )
                            }
                        }
                    }
                }
            }
            VerticalDivider()
            NavHost(
                innerNavController,
                startDestination = InnerNavRoutes.Root,
                modifier = Modifier.clipToBounds(),
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
                composable<InnerNavRoutes.Root> {
                    isShowingSubFolder.value = false
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Click a folder to see its content")

                    }
                }
                composable<InnerNavRoutes.Account> {
                    isShowingSubFolder.value = true
                    Account(navController = innerNavController, backNavigation = { innerNavController.navigate(InnerNavRoutes.Root) })
                }
                composable<InnerNavRoutes.Appearance> {
                    isShowingSubFolder.value = true
                    Appearance(navController = innerNavController, backNavigation = { innerNavController.navigate(InnerNavRoutes.Root) })
                }
            }
        }
    }
}

@Serializable
sealed class InnerNavRoutes {
    @Serializable
    data object Root : InnerNavRoutes()
    @Serializable
    data object Account : InnerNavRoutes()
    @Serializable
    data object Appearance : InnerNavRoutes()
}