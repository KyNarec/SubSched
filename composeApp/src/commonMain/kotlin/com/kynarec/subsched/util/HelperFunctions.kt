package com.kynarec.subsched.util

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

operator fun TextUnit.plus(other: TextUnit): TextUnit = (this.value + other.value).sp