package com.kynarec.subsched.util

enum class ScrollSpeed(val delay: Long, val pixelsPerFrame: Float) {
    VerySlow(16, 0.7f),
    Slow(16, 1f),
    Medium(16, 1.5f),
    Fast(8, 2f),
    VeryFast(8, 2.5f),
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