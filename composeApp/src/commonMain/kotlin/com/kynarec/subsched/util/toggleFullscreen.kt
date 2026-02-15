package com.kynarec.subsched.util

import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState

fun WindowState.toggleFullscreen() {
    this.placement = if (this.placement == WindowPlacement.Fullscreen) {
        WindowPlacement.Floating
    } else {
        WindowPlacement.Fullscreen
    }
}