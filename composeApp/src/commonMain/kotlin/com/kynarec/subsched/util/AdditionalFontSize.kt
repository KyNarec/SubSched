package com.kynarec.subsched.util

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class AdditionalFontSize(val fontSize: TextUnit) {
    ExtraSmall(2.sp),
    Small(3.sp),
    Medium(5.sp),
    Large(7.sp),
    ExtraLarge(10.sp),
    TurboLarge(12.sp),
;
    override fun toString(): String {
        return when (this) {
            ExtraSmall -> "Extra small"
            Small -> "Small"
            Medium -> "Medium"
            Large -> "Large"
            ExtraLarge -> "Extra large"
            TurboLarge -> "Turbo large"
        }
    }
}