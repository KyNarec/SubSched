package com.kynarec.subsched.util

enum class ScrollSpeed(val pixelsPerSecond: Float) {
    VerySlow(30f),
    Slow(60f),
    Medium(100f),
    Fast(180f),
    VeryFast(250f),
;
    override fun toString(): String {
        return when (this) {
            VerySlow -> "Very slow"
            Slow -> "Slow"
            Medium -> "Medium"
            Fast -> "Fast"
            VeryFast -> "Very fast"
        }
    }
}