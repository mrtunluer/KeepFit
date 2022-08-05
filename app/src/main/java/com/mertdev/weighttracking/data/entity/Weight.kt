package com.mertdev.weighttracking.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Weight (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val value: Float?,
    val date: Date?,
    val note: String?
): Serializable