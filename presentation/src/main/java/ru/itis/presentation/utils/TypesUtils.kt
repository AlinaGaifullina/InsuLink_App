 package ru.itis.presentation.utils

import kotlin.math.roundToInt

fun Float.roundToOneDecimal(): Float {
    return (this * 10).roundToInt() / 10f
}