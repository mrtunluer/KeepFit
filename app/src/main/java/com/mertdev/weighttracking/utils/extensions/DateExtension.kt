package com.mertdev.weighttracking.utils.extensions

import com.mertdev.weighttracking.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

fun Date.showDate(): String? =
    SimpleDateFormat(Constants.DATE_PATTERN, Locale.getDefault()).format(this)

fun Date.startOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.apply {
        time = this@startOfDay
        set(Calendar.SECOND, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }
    return calendar.time
}

fun Date.endOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.apply {
        time = this@endOfDay
        set(Calendar.SECOND, 59)
        set(Calendar.MINUTE, 59)
        set(Calendar.HOUR_OF_DAY, 23)
    }
    return calendar.time
}