package com.mertdev.keepfit.ui.home

import android.content.Context
import com.mertdev.keepfit.R
import com.mertdev.keepfit.databinding.FragmentHomeBinding
import com.mertdev.keepfit.uimodel.WeightUiModel
import com.mertdev.keepfit.utils.Constants.FT
import com.mertdev.keepfit.utils.Constants.LB
import com.mertdev.keepfit.utils.Constants.MALE
import com.mertdev.keepfit.utils.extensions.*
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object MathematicalOperations {

    fun setHorizontalProgressLoading(data: WeightUiModel, binding: FragmentHomeBinding) =
        with(data) {
            if (firstWeight != null && currentWeight != null && targetWeight != null) {
                val min = min(min(firstWeight!!, currentWeight!!), targetWeight!!)
                val max = max(max(firstWeight!!, currentWeight!!), targetWeight!!)
                val median = max(
                    min(firstWeight!!, currentWeight!!),
                    min(max(firstWeight!!, currentWeight!!), targetWeight!!)
                )

                val progressMax = firstWeight!!.minus(targetWeight!!).absoluteValue

                if (currentWeight == median && firstWeight!! > targetWeight!!) {
                    val progress = median.minus(max).absoluteValue
                    binding.horizontalProgress.progress =
                        progress.div(progressMax).times(100).toInt()
                } else if (currentWeight == median && targetWeight!! > firstWeight!!) {
                    val progress = median.minus(min).absoluteValue
                    binding.horizontalProgress.progress =
                        progress.div(progressMax).times(100).toInt()
                } else if (min == currentWeight && max == firstWeight) binding.horizontalProgress.progress =
                    100
                else if (max == currentWeight && min == firstWeight) binding.horizontalProgress.progress =
                    100
                else binding.horizontalProgress.progress = 0
            }
        }

    fun calculateBmi(data: WeightUiModel, binding: FragmentHomeBinding) = with(data) {
        val weight: Float? = if (weightUnit == LB) currentWeight?.toKg()
        else currentWeight

        val height: Float? = if (heightUnit == FT) height?.toCm()?.div(100)?.pow(2)
        else height?.div(100)?.pow(2)

        bmi = height?.let { weight?.div(it)?.round(1) }

        binding.bmiTxt.text = bmi.toString()
    }

    fun calculateIdealWeight(data: WeightUiModel, binding: FragmentHomeBinding) = with(data) {
        val height: Float? = if (heightUnit == FT) height?.toCm()
        else height

        val idealWeight: Float? = if (gender == MALE) height?.idealWeightForMale()
        else height?.idealWeightForFemale()

        binding.idealWeightTxt.text = if (weightUnit == LB) idealWeight?.toLb().toString()
        else idealWeight?.toString()
    }

    fun calculateHealthyWeightRange(data: WeightUiModel, binding: FragmentHomeBinding) =
        with(data) {
            val height: Float? = if (heightUnit == FT) height?.toCm()?.div(100)?.pow(2)
            else height?.div(100)?.pow(2)

            val firstWeight = height?.firstWeightOfHealthyWeightRange()
            val lastWeight = height?.lastWeightOfHealthyWeightRange()

            binding.healthyWeightRangeTxt.text =
                if (weightUnit == LB) firstWeight?.toLb().toString()
                    .plus(" - " + lastWeight?.toLb())
                else firstWeight?.toString().plus(" - $lastWeight")
        }

    fun setRemainderWeight(data: WeightUiModel, binding: FragmentHomeBinding, context: Context) {
        binding.remainingTxt.text =
            context.getString(R.string.remaining).plus(" " + data.currentWeight?.let {
                data.targetWeight?.minus(it)?.round(1)?.absoluteValue
            })
    }

}