package com.kynarec.subsched.util

interface WindowHandler {
    val isFullscreen: Boolean
    suspend fun toggleFullscreen()
}