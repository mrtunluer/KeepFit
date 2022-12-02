package com.mertdev.keepfit.uimodel

import com.mertdev.keepfit.data.entity.Weight
import java.io.Serializable

data class WeightUiModel(
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
    var isShowEmptyLayout: Boolean? = null
) : Serializable