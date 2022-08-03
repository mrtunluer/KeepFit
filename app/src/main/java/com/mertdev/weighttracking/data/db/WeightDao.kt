package com.mertdev.weighttracking.data.db

import androidx.room.*
import com.mertdev.weighttracking.data.entity.Weight
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WeightDao {

    @Query("SELECT * FROM Weight ORDER BY date ASC")
    fun getAllWeights(): Flow<List<Weight>>

    // start and end of the entered weight's day (date)
    @Query("SELECT * FROM Weight WHERE date BETWEEN :start AND :end")
    fun getWeightByDate(start: Date, end: Date): Flow<Weight?>

    @Query("SELECT AVG(weight) FROM Weight")
    fun getAvgWeight(): Flow<Float?>

    @Query("SELECT MAX(weight) FROM Weight")
    fun getMaxWeight(): Flow<Float?>

    @Query("SELECT MIN(weight) FROM Weight")
    fun getMinWeight(): Flow<Float?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: Weight)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeight(weight: Weight)

    @Query("DELETE FROM Weight where id = :id")
    suspend fun deleteWeight(id: Int)

}