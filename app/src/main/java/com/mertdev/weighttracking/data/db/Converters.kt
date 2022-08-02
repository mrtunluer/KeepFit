package com.mertdev.weighttracking.data.db

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}