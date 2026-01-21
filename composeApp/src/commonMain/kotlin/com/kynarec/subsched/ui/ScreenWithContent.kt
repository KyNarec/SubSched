package com.kynarec.subsched.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ScreenWithContent(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
    )
    { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            content()
        }
    }
}