package com.kynarec.subsched

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() {
    initKoin()
    application {
        val state = rememberWindowState(placement = WindowPlacement.Maximized)
        Window(
            onCloseRequest = ::exitApplication,
            state = state
        ) {
//            LaunchedEffect(state.size) {
//                println(state.placement.name)
//                println(state.size.height)
//                println(state.size.width)
//            }
            App()
        }
    }
}