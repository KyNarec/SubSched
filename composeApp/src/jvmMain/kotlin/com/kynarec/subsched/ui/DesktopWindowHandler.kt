package com.kynarec.subsched.ui

import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.WindowPlacement
import com.kynarec.subsched.util.WindowHandler
import kotlinx.coroutines.delay

class DesktopWindowHandler(private val state: WindowState) : WindowHandler {
    override val isFullscreen: Boolean
        get() = state.placement == WindowPlacement.Fullscreen

    override suspend fun toggleFullscreen() {
        state.toggleFullscreen()
    }
}

suspend fun WindowState.toggleFullscreen() {
    if (placement == WindowPlacement.Fullscreen) {
        placement = WindowPlacement.Floating

        delay(50)

        placement = WindowPlacement.Maximized
    } else {
        placement = WindowPlacement.Fullscreen
    }
}