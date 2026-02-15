package com.kynarec.subsched.ui.screens.home.misc

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

@Composable
actual fun rememberWindowInfo(): WindowInfo {
    // 1. Create a state holder that Compose can track
    val windowInfoState = remember {
        mutableStateOf(
            WindowInfo(
                WindowInfo.WindowType.Compact,
                WindowInfo.WindowType.Compact,
                0.dp, 0.dp
            )
        )
    }

    // 2. Use BoxWithConstraints to update that state
    BoxWithConstraints {
        // This block runs every time the window resizes
        val width = maxWidth
        val height = maxHeight

        // Update the state object
        windowInfoState.value = WindowInfo(
            screenWidthInfo = when {
                width < 600.dp -> WindowInfo.WindowType.Compact
                width < 1355.dp -> WindowInfo.WindowType.Medium
                else -> WindowInfo.WindowType.Expanded
            },
            screenHeightInfo = when {
                height < 480.dp -> WindowInfo.WindowType.Compact
                height < 900.dp -> WindowInfo.WindowType.Medium
                else -> WindowInfo.WindowType.Expanded
            },
            screenWidth = width,
            screenHeight = height
        )
    }

    // 3. Return the value of the state
    return windowInfoState.value
}