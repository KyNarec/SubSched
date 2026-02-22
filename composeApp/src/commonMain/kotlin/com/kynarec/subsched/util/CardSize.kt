package com.kynarec.subsched.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class CardSize(val width: Dp, val defaultFontSize: TextUnit) {
    ExtraSmall(430.dp, 8.sp),
    Small(480.dp, 9.sp),
    Medium(500.dp, 10.sp),
    Large(550.dp, 12.sp),
    ExtraLarge(600.dp, 13.sp),
    TurboLarge(700.dp, 14.sp),
}