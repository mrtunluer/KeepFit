package com.mertdev.weighttracking.uimodel

import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.data.entity.MeasurementContent
import java.io.Serializable

data class MeasurementUiModel(
    var measurementId: Int? = null,
    var measurementName: String? = null,
    var allMeasurements: List<Measurement> = emptyList(),
    var allMeasurementContent: List<MeasurementContent> = emptyList(),
    var lastSevenMeasurementContent: List<MeasurementContent> = emptyList(),
    var measurementContent: MeasurementContent? = null,
    var maxMeasurementContentValue: Float? = null,
    var minMeasurementContentValue: Float? = null,
    var avgMeasurementContentValue: Float? = null,
    var currentMeasurementContentValue: Float? = null,
    var lengthUnit: String? = null,
    var numberOfChartData: Int? = null,
    var isShowEmptyLayout: Boolean? = null
): Serializable