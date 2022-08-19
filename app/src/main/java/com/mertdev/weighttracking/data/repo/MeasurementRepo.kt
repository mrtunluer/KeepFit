package com.mertdev.weighttracking.data.repo

import com.mertdev.weighttracking.data.db.UserInfoDao
import javax.inject.Inject

class MeasurementRepo @Inject constructor(private val dao: UserInfoDao) {

}