package com.mertdev.weighttracking.data.repo

import com.mertdev.weighttracking.data.db.UserInfoDao
import com.mertdev.weighttracking.data.entity.Measurement
import java.util.*
import javax.inject.Inject

class MeasurementRepo @Inject constructor(private val dao: UserInfoDao) {

    fun getAllMeasurements() = dao.getAllMeasurements()

    fun getMeasurementByDate(start: Date, end: Date) = dao.getMeasurementByDate(start, end)

    suspend fun updateMeasurement(measurement: Measurement) = dao.updateMeasurement(measurement)

    suspend fun insertMeasurement(measurement: Measurement) = dao.insertMeasurement(measurement)

}