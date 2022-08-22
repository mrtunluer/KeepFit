package com.mertdev.weighttracking.data.db

import androidx.room.*
import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.data.entity.MeasurementContent
import com.mertdev.weighttracking.data.entity.Weight
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface UserInfoDao {

    // Weight Query

    @Query("SELECT * FROM Weight ORDER BY date ASC")
    fun getAllWeights(): Flow<List<Weight>>

    // start and end of the entered weight's day (date)
    @Query("SELECT * FROM Weight WHERE date BETWEEN :start AND :end")
    fun getWeightByDate(start: Date, end: Date): Flow<Weight?>

    @Query("SELECT AVG(value) FROM Weight")
    fun getAvgWeight(): Flow<Float?>

    @Query("SELECT MAX(value) FROM Weight")
    fun getMaxWeight(): Flow<Float?>

    @Query("SELECT MIN(value) FROM Weight")
    fun getMinWeight(): Flow<Float?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: Weight)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeight(weight: Weight)

    @Query("DELETE FROM Weight where id = :id")
    suspend fun deleteWeight(id: Int)

    // Measurement Query

    @Query("SELECT * FROM Measurement ORDER BY date DESC")
    fun getAllMeasurements(): Flow<List<Measurement>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMeasurement(measurement: Measurement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: Measurement)

    @Query("DELETE FROM Measurement where id = :id")
    suspend fun deleteMeasurement(id: Int)

    @Query("SELECT * FROM MeasurementContent INNER JOIN Measurement ON Measurement.id = MeasurementContent.measurementId WHERE Measurement.id = :id ORDER BY MeasurementContent.date DESC")
    fun getMeasurementContent(id: Int): Flow<List<MeasurementContent>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMeasurementContent(measurementContent: MeasurementContent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurementContent(measurementContent: MeasurementContent)

    @Query("DELETE FROM MeasurementContent where id = :id")
    suspend fun deleteMeasurementContent(id: Int)

}