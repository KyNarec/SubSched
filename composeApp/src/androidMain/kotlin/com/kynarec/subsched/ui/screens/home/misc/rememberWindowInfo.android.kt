package com.kynarec.subsched.ui.screens.home.misc

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@Composable
actual fun rememberWindowInfo(): WindowInfo {
    val width = LocalWindowInfo.current.containerSize.width
    val height = LocalWindowInfo.current.containerSize.height
    return WindowInfo(
        screenWidthInfo = when {
            width < 600 -> WindowInfo.WindowType.Compact
            height < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenHeightInfo = when {
            width < 480 -> WindowInfo.WindowType.Compact
            height < 900 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenWidth = width.dp,
        screenHeight = height.dp
    )
}