package com.kynarec.subsched

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kynarec.subsched.di.initKoin
import com.kynarec.subsched.ui.DesktopWindowHandler
import com.kynarec.subsched.ui.toggleFullscreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import subsched.composeapp.generated.resources.Res
import subsched.composeapp.generated.resources.ic_subsched_logo

fun main() {
    initKoin()
    application {
        val state = rememberWindowState(placement = WindowPlacement.Maximized)
        val scope = rememberCoroutineScope()
        Window(
            onCloseRequest = ::exitApplication,
            state = state,
            title = "SubSched",
            icon = painterResource(Res.drawable.ic_subsched_logo),
            onKeyEvent = {
                if (it.type == KeyEventType.KeyDown && it.key == Key.F11) {
                    scope.launch {
                        state.toggleFullscreen()
                    }
                    true
                } else {
                    false
                }
            }
        ) {
//            LaunchedEffect(state.size) {
//                println(state.placement.name)
//                println(state.size.height)
//                println(state.size.width)
//            }
            val windowHandler = remember(state) { DesktopWindowHandler(state) }
            App(
                windowHandler = windowHandler
            )
        }
    }
}