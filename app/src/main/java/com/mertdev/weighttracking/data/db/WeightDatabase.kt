package com.mertdev.weighttracking.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mertdev.weighttracking.data.entity.Weight

@Database(
    entities = [Weight::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class WeightDatabase: RoomDatabase() {
    companion object{
        const val DB_NAME = "weight_db"
    }
    abstract fun getWeightDao(): WeightDao
}