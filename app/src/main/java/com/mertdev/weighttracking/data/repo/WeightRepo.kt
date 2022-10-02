package com.mertdev.weighttracking.data.repo

import com.mertdev.weighttracking.data.db.UserInfoDao
import com.mertdev.weighttracking.data.entity.Weight
import java.util.*
import javax.inject.Inject

class WeightRepo @Inject constructor(private val dao: UserInfoDao) {

    fun getAllWeights() = dao.getAllWeights()

    fun getWeightByDate(start: Date, end: Date) = dao.getWeightByDate(start, end)

    fun getAvgWeight() = dao.getAvgWeight()

    fun getMaxWeight() = dao.getMaxWeight()

    fun getMinWeight() = dao.getMinWeight()

    suspend fun insertWeight(weight: Weight) = dao.insertWeight(weight)

    suspend fun updateWeight(weight: Weight) = dao.updateWeight(weight)

    suspend fun deleteWeight(id: Int) = dao.deleteWeight(id)

    suspend fun deleteWeightTable() = dao.deleteWeightTable()

}