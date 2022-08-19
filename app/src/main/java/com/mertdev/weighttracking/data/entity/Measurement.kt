package com.mertdev.weighttracking.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.*

@Entity
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String?
)

@Entity
data class MeasurementContent(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val measurementId: Int?,
    val value: Float?,
    val date: Date?,
    val note: String?
)

data class MeasurementWithMeasurementContent(
    @Embedded val measurement:Measurement,
    @Relation(
        parentColumn = "id",
        entityColumn = "measurementId"
    )
    val measurementContent: List<MeasurementContent>
)