package com.kynarec.subsched.util

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowSize
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