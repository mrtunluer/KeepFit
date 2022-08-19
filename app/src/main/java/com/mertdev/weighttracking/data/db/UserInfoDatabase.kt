package com.mertdev.weighttracking.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.data.entity.MeasurementContent
import com.mertdev.weighttracking.data.entity.Weight

@Database(
    entities = [
        Weight::class,
        Measurement::class,
        MeasurementContent::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class UserInfoDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "user_info_db"
    }

    abstract fun getUserInfoDao(): UserInfoDao
}