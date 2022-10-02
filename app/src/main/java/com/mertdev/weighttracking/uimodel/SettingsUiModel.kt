package com.mertdev.weighttracking.uimodel

import java.io.Serializable

data class SettingsUiModel(
    var targetWeight: Float? = null,
    var weightUnit: String? = null,
    var height: Float? = null,
    var heightUnit: String? = null,
    var gender: String? = null,
    var numberOfChartData: Int? = null,
    var isDeleteAllWeightData: Boolean? = null,
    var isDeleteAllMeasurementData: Boolean? = null,
    var isShare: Boolean? = null,
    var isRateUsOnGooglePlay: Boolean? = null
): Serializable