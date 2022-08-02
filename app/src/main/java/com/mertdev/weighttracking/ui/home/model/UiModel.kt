package com.mertdev.weighttracking.ui.home.model

import com.mertdev.weighttracking.data.entity.Weight
import java.io.Serializable

data class UiModel(
    var weight: Weight? = null,
    var firstWeight: Float? = null,
    var currentWeight: Float? = null,
    var allWeights: List<Weight?> = emptyList(),
    var targetWeight: Float? = null,
    var weightUnit: String? = null
): Serializable