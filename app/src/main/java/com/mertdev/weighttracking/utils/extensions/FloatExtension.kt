package com.mertdev.weighttracking.utils.extensions

import com.mertdev.weighttracking.utils.Constants.FT_TO_CM
import com.mertdev.weighttracking.utils.Constants.LB_TO_KG

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier).div(multiplier).toFloat()
}

fun Float.toKg(): Float {
    return this.times(LB_TO_KG).round(1)
}

fun Float.toCm(): Float {
    return this.times(FT_TO_CM).round(1)
}