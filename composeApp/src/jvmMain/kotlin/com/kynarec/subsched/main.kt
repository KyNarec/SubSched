package com.kynarec.subsched

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.compose.resources.painterResource
import subsched.composeapp.generated.resources.Res
import subsched.composeapp.generated.resources.ic_subsched_logo
import java.io.File

fun main() {
    initKoin()
    application {
        val state = rememberWindowState(placement = WindowPlacement.Maximized)
        Window(
            onCloseRequest = ::exitApplication,
            state = state,
            title = "SubSched",
            icon = painterResource(Res.drawable.ic_subsched_logo)
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