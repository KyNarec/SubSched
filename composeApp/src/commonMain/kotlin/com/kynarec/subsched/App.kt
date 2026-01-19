package com.kynarec.subsched

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.kynarec.subsched.ui.navigation.Navigation
import com.kynarec.subsched.ui.theme.SubSchedTheme

@Composable
@Preview
fun App() {
    SubSchedTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        val navController = rememberNavController()
        Navigation(navController)
    }
}