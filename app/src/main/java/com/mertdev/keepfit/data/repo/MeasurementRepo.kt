package com.mertdev.keepfit.data.repo

import com.mertdev.keepfit.data.db.UserInfoDao
import com.mertdev.keepfit.data.entity.Measurement
import com.mertdev.keepfit.data.entity.MeasurementContent
import java.util.*
import javax.inject.Inject

class MeasurementRepo @Inject constructor(private val dao: UserInfoDao) {

    fun getAllMeasurements() = dao.getAllMeasurements()

    suspend fun updateMeasurement(measurement: Measurement) = dao.updateMeasurement(measurement)

    suspend fun insertMeasurement(measurement: Measurement) = dao.insertMeasurement(measurement)

    suspend fun deleteMeasurement(id: Int) = dao.deleteMeasurement(id)

    suspend fun deleteMeasurementTable() = dao.deleteMeasurementTable()

    fun getMeasurementContent(id: Int) = dao.getMeasurementContent(id)

    fun getMeasurementContentByDate(id: Int, start: Date, end: Date) =
        dao.getMeasurementContentByDate(id, start, end)

    fun getAvgMeasurementContentValue(measurementId: Int) =
        dao.getAvgMeasurementContentValue(measurementId)

    fun getMaxMeasurementContentValue(measurementId: Int) =
        dao.getMaxMeasurementContentValue(measurementId)

    fun getMinMeasurementContentValue(measurementId: Int) =
        dao.getMinMeasurementContentValue(measurementId)

    suspend fun updateMeasurementContent(measurementContent: MeasurementContent) =
        dao.updateMeasurementContent(measurementContent)

    suspend fun insertMeasurementContent(measurementContent: MeasurementContent) =
        dao.insertMeasurementContent(measurementContent)

    suspend fun deleteMeasurementContent(id: Int) = dao.deleteMeasurementContent(id)

    suspend fun deleteMeasurementContentTable() = dao.deleteMeasurementContentTable()
}