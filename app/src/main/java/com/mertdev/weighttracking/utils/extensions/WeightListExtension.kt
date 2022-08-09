package com.mertdev.weighttracking.utils.extensions

import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.utils.Constants.LIMIT_FOR_STATISTICS

fun List<Weight>.lastSevenWeight(): MutableList<Weight> {
    val lastSevenList = mutableListOf<Weight>()
    if (this.size >= LIMIT_FOR_STATISTICS)
        for (i in 0 until LIMIT_FOR_STATISTICS){
            lastSevenList.add(this[i])
        }
    else
        for (i in 0 until this.size){
            lastSevenList.add(this[i])
        }
    return lastSevenList
}