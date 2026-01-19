package com.kynarec.subsched

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform