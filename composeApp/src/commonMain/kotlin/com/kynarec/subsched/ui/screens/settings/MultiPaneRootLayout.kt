package com.kynarec.subsched.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kynarec.subsched.ui.navigation.NavRoutes
import com.kynarec.subsched.ui.screens.settings.misc.SettingsFolder

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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            Modifier.fillMaxSize()
                .padding(paddingValues = contentPadding)
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
                                onClick = { navController.navigate(NavRoutes.Settings.Account) }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                    item {
                        ElevatedCard {
                            SettingsFolder(
                                title = { Text("Appearance") },
                                icon = { Icon(Icons.Rounded.Palette, null) },
                                onClick = { navController.navigate(NavRoutes.Settings.Appearance) }
                            )
                        }
                    }
                }
            }
        }
    }
}