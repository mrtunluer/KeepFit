package com.mertdev.weighttracking.utils.extensions

import com.mertdev.weighttracking.utils.Constants.FT_TO_CM
import com.mertdev.weighttracking.utils.Constants.LB_TO_KG

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier).div(multiplier).toFloat()
}

fun Float.toKg() =
    this.times(LB_TO_KG).round(1)

fun Float.toLb() =
    this.div(LB_TO_KG).round(1)

fun Float.toCm() =
    this.times(FT_TO_CM).round(1)

fun Float.idealWeightForMale() =
    this.div(2.54).minus(60).times(2.3).plus(50).toFloat().round(1)

fun Float.idealWeightForFemale() =
    this.div(2.54).minus(60).times(2.3).plus(45.5).toFloat().round(1)