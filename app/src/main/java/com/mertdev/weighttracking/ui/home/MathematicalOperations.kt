package com.mertdev.weighttracking.ui.home

import android.content.Context
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentHomeBinding
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.Constants.FT
import com.mertdev.weighttracking.utils.Constants.LB
import com.mertdev.weighttracking.utils.Constants.MALE
import com.mertdev.weighttracking.utils.extensions.*
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object MathematicalOperations {

    fun setHorizontalProgressLoading(data: UiModel, binding: FragmentHomeBinding) =
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
                } else if (min == currentWeight && max == firstWeight)
                    binding.horizontalProgress.progress = 100
                else if (max == currentWeight && min == firstWeight)
                    binding.horizontalProgress.progress = 100
                else
                    binding.horizontalProgress.progress = 0
            }
        }

    fun calculateBmi(data: UiModel, binding: FragmentHomeBinding) = with(data) {
        val weight: Float? = if (weightUnit == LB)
            currentWeight?.toKg()
        else
            currentWeight

        val height: Float? = if (heightUnit == FT)
            height?.toCm()?.div(100)?.pow(2)
        else
            height?.div(100)?.pow(2)

        bmi = height?.let { weight?.div(it)?.round(1) }

        binding.bmiTxt.text = bmi.toString()
    }

    fun calculateIdealWeight(data: UiModel, binding: FragmentHomeBinding) = with(data) {
        val height: Float? = if (heightUnit == FT)
            height?.toCm()
        else
            height

        val idealWeight: Float? = if (gender == MALE)
            height?.idealWeightForMale()
        else
            height?.idealWeightForFemale()

        binding.idealWeightTxt.text = if (weightUnit == LB)
            idealWeight?.toLb().toString()
        else
            idealWeight?.toString()
    }

    fun calculateHealthyWeightRange(data: UiModel, binding: FragmentHomeBinding) =
        with(data) {
            val height: Float? = if (heightUnit == FT)
                height?.toCm()?.div(100)?.pow(2)
            else
                height?.div(100)?.pow(2)

            val firstWeight = height?.firstWeightOfHealthyWeightRange()
            val lastWeight = height?.lastWeightOfHealthyWeightRange()

            binding.healthyWeightRangeTxt.text = if (weightUnit == LB)
                firstWeight?.toLb().toString().plus(" - " + lastWeight?.toLb())
            else
                firstWeight?.toString().plus(" - $lastWeight")
        }

    fun setRemainderWeight(data: UiModel, binding: FragmentHomeBinding, context: Context) {
        binding.remainingTxt.text =
            context.getString(R.string.remaining).plus(" " + data.currentWeight?.let {
                data.targetWeight?.minus(it)?.round(1)?.absoluteValue
            })
    }

}