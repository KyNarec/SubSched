package com.kynarec.subsched.ui.screens.home.misc

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
expect fun rememberWindowInfo(): WindowInfo

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class WindowType {
        object Compact: WindowType()
        object Medium: WindowType()
        object Expanded: WindowType()
    }
}