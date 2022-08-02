package com.mertdev.weighttracking.data.db

import androidx.room.*
import com.mertdev.weighttracking.data.entity.Weight
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WeightDao {

    @Query("SELECT * FROM Weight ORDER BY date DESC")
    fun getAllWeights(): Flow<List<Weight>>

    @Query("SELECT * FROM Weight ORDER BY date ASC LIMIT 1")
    fun getFirstWeight(): Flow<Weight>

    @Query("SELECT * FROM Weight ORDER BY date DESC LIMIT 1")
    fun getCurrentWeight(): Flow<Weight>

    // start and end of the entered weight's day (date)
    @Query("SELECT * FROM weight WHERE date BETWEEN :start AND :end")
    fun getWeightByDate(start: Date, end: Date): Flow<Weight?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: Weight)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeight(weight: Weight)

    @Query("DELETE FROM Weight where id = :id")
    suspend fun deleteWeight(id: Int)

}