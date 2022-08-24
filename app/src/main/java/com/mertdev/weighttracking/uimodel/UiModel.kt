package com.mertdev.weighttracking.uimodel

import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.data.entity.MeasurementContent
import com.mertdev.weighttracking.data.entity.Weight
import java.io.Serializable

data class UiModel(
    var allWeights: List<Weight> = emptyList(),
    var lastSevenWeight: List<Weight> = emptyList(),
    var weight: Weight? = null,
    var firstWeight: Float? = null,
    var currentWeight: Float? = null,
    var maxWeight: Float? = null,
    var minWeight: Float? = null,
    var avgWeight: Float? = null,
    var targetWeight: Float? = null,
    var height: Float? = null,
    var gender: String? = null,
    var heightUnit: String? = null,
    var weightUnit: String? = null,
    var numberOfChartData: Int? = null,
    var bmi: Float? = null,
    var isShowEmptyLayout: Boolean? = null,
    var allMeasurements: List<Measurement> = emptyList(),
    var allMeasurementContent: List<MeasurementContent> = emptyList(),
    var lastSevenMeasurementContent: List<MeasurementContent> = emptyList(),
    var maxMeasurementContentValue: Float? = null,
    var minMeasurementContentValue: Float? = null,
    var avgMeasurementContentValue: Float? = null
): Serializable