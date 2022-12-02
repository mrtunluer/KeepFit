package com.mertdev.keepfit.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String?,
    val lengthUnit: String?,
    val date: Date?
) : Serializable

@Entity
data class MeasurementContent(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val measurementId: Int?,
    val value: Float?,
    val date: Date?,
    val note: String?
)