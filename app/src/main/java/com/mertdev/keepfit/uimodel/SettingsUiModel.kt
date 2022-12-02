package com.mertdev.keepfit.uimodel

import java.io.Serializable

data class SettingsUiModel(
    var targetWeight: Float? = null,
    var weightUnit: String? = null,
    var height: Float? = null,
    var heightUnit: String? = null,
    var gender: String? = null,
    var numberOfChartData: Int? = null,
    var isDeleteAllWeightData: Boolean? = null,
    var isDeleteAllMeasurementData: Boolean? = null
) : Serializable