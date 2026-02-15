package com.kynarec.subsched.util

import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.delay

suspend fun WindowState.toggleFullscreen() {
    if (placement == WindowPlacement.Fullscreen) {
        placement = WindowPlacement.Floating

        delay(50)

        placement = WindowPlacement.Maximized
    } else {
        placement = WindowPlacement.Fullscreen
    }
}