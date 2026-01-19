package com.kynarec.subsched

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    initKoin()
    application {
        Window(onCloseRequest = ::exitApplication) {
            App()
        }
    }
}