package com.kynarec.subsched

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kynarec.subsched.ui.navigation.Navigation
import com.kynarec.subsched.ui.theme.SubSchedTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<SubSchedViewModel>()
    val darkTheme = if (viewModel.isFirstLaunch) isSystemInDarkTheme() else viewModel.darkThemeFlow.collectAsStateWithLifecycle(true).value
    viewModel.isFirstLaunch = false
    SubSchedTheme(
        darkTheme = darkTheme
    ) {
        val navController = rememberNavController()
        Navigation(navController)
    }
}