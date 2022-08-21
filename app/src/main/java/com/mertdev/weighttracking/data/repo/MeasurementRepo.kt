package com.mertdev.weighttracking.data.repo

import com.mertdev.weighttracking.data.db.UserInfoDao
import com.mertdev.weighttracking.data.entity.Measurement
import javax.inject.Inject

class MeasurementRepo @Inject constructor(private val dao: UserInfoDao) {

    fun getAllMeasurements() = dao.getAllMeasurements()

    suspend fun updateMeasurement(measurement: Measurement) = dao.updateMeasurement(measurement)

    suspend fun insertMeasurement(measurement: Measurement) = dao.insertMeasurement(measurement)

    suspend fun deleteMeasurement(id: Int) = dao.deleteMeasurement(id)

}