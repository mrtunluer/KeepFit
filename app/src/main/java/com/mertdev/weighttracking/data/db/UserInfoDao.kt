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

    @Query("DELETE FROM Weight")
    suspend fun deleteWeightTable()

    // Measurement Query

    @Query("SELECT * FROM Measurement ORDER BY date DESC")
    fun getAllMeasurements(): Flow<List<Measurement>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMeasurement(measurement: Measurement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: Measurement)

    @Query("DELETE FROM Measurement where id = :id")
    suspend fun deleteMeasurement(id: Int)

    @Query("SELECT MeasurementContent.id, MeasurementContent.measurementId, MeasurementContent.value, MeasurementContent.date, MeasurementContent.note FROM MeasurementContent INNER JOIN Measurement ON Measurement.id = MeasurementContent.measurementId WHERE Measurement.id = :id ORDER BY MeasurementContent.date ASC")
    fun getMeasurementContent(id: Int): Flow<List<MeasurementContent>>

    // start and end of the entered measurement content data's day (date)
    @Query("SELECT * FROM MeasurementContent WHERE measurementId = :id AND (MeasurementContent.date BETWEEN :start AND :end)")
    fun getMeasurementContentByDate(id: Int, start: Date, end: Date): Flow<MeasurementContent?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMeasurementContent(measurementContent: MeasurementContent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurementContent(measurementContent: MeasurementContent)

    @Query("DELETE FROM MeasurementContent where id = :id")
    suspend fun deleteMeasurementContent(id: Int)

    @Query("SELECT AVG(value) FROM MeasurementContent WHERE measurementId = :measurementId")
    fun getAvgMeasurementContentValue(measurementId: Int): Flow<Float?>

    @Query("SELECT MAX(value) FROM MeasurementContent WHERE measurementId = :measurementId")
    fun getMaxMeasurementContentValue(measurementId: Int): Flow<Float?>

    @Query("SELECT MIN(value) FROM MeasurementContent WHERE measurementId = :measurementId")
    fun getMinMeasurementContentValue(measurementId: Int): Flow<Float?>

}