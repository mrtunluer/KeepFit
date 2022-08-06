package com.mertdev.weighttracking.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentHomeBinding
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.Constants.FT
import com.mertdev.weighttracking.utils.Constants.LB
import com.mertdev.weighttracking.utils.Constants.MALE
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModels()
    private var uiModel = UiModel()
    private val statisticsAdapter = StatisticsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

        binding.addBtn.setOnClickListener {
            findNavController().safeNavigate(
                HomeFragmentDirections.actionHomeFragmentToAddWeightFragment(
                    uiModel
                )
            )
        }

        statisticsAdapter.setOnItemClickListener { weight ->
            uiModel = uiModel.copy(weight = weight)
            findNavController().safeNavigate(
                HomeFragmentDirections.actionHomeFragmentToAddWeightFragment(
                    uiModel
                )
            )
        }

    }

    private fun initView() {
        binding.swipeRefresh.isEnabled = false
        binding.horizontalProgress.max = 100

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.rv_divider_layer
            )!!
        )

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = statisticsAdapter
            addItemDecoration(itemDecoration)
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { dataStatus ->
                when (dataStatus) {
                    is DataStatus.Loading -> onLoadingForUiState()
                    is DataStatus.Error -> onErrorForUiState()
                    is DataStatus.Success -> dataStatus.data?.let { onSuccessForUiState(it) }
                }
            }
    }

    private fun onLoadingForUiState() {
        binding.swipeRefresh.isRefreshing = true
        binding.errorTxt.isVisible = false
    }

    private fun onErrorForUiState() {
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = true
    }

    private fun onSuccessForUiState(data: UiModel) = with(data) {
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = false
        binding.targetWeightTxt.text = targetWeight.toString()
        binding.currentWeightTxt.text = currentWeight.toString()
        binding.firstWeightTxt.text = firstWeight.toString()
        binding.weightUnitTxt.text = weightUnit
        binding.maxWeightTxt.text = maxWeight.toString()
        binding.minWeightTxt.text = minWeight.toString()
        binding.avgWeightTxt.text = avgWeight?.round(1).toString()
        statisticsAdapter.submitList(allWeights)
        setRemainderWeight(this)
        setHorizontalProgressLoading(this)
        calculateBmi(this)
        calculateIdealWeight(this)
        calculateHealthyWeightRange(this)
        uiModel = this
    }

    private fun setRemainderWeight(data: UiModel) {
        binding.remainingTxt.text =
            getString(R.string.remaining).plus(" " + data.currentWeight?.let {
                data.targetWeight?.minus(it)?.round(1)?.absoluteValue
            })
    }

    private fun setHorizontalProgressLoading(data: UiModel) = with(data) {
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
                binding.horizontalProgress.progress = progress.div(progressMax).times(100).toInt()
            } else if (currentWeight == median && targetWeight!! > firstWeight!!) {
                val progress = median.minus(min).absoluteValue
                binding.horizontalProgress.progress = progress.div(progressMax).times(100).toInt()
            } else if (min == currentWeight && max == firstWeight)
                binding.horizontalProgress.progress = 100
            else if (max == currentWeight && min == firstWeight)
                binding.horizontalProgress.progress = 100
            else
                binding.horizontalProgress.progress = 0
        }
    }

    private fun calculateBmi(data: UiModel) {
        val weight: Float? = if (data.weightUnit == LB)
            data.currentWeight?.toKg()
        else
            data.currentWeight

        val height: Float? = if (data.heightUnit == FT)
            data.height?.toCm()?.div(100)?.pow(2)
        else
            data.height?.div(100)?.pow(2)

        val bmi = height?.let { weight?.div(it)?.round(1) }
        binding.bmiTxt.text = bmi.toString()
    }

    private fun calculateIdealWeight(data: UiModel) {
        val height: Float? = if (data.heightUnit == FT)
            data.height?.toCm()
        else
            data.height

        val idealWeight: Float? = if (data.gender == MALE)
            height?.idealWeightForMale()
        else
            height?.idealWeightForFemale()

        binding.idealWeightTxt.text = if (data.weightUnit == LB)
            idealWeight?.toLb().toString()
        else
            idealWeight?.toString()
    }

    private fun calculateHealthyWeightRange(data: UiModel) {
        val height: Float? = if (data.heightUnit == FT)
            data.height?.toCm()?.div(100)?.pow(2)
        else
            data.height?.div(100)?.pow(2)

        val firstWeight = height?.firstWeightOfHealthyWeightRange()
        val lastWeight = height?.lastWeightOfHealthyWeightRange()

        binding.healthyWeightRangeTxt.text = if (data.weightUnit == LB)
            firstWeight?.toLb().toString().plus(" - " + lastWeight?.toLb())
        else
            firstWeight?.toString().plus(" - $lastWeight")
    }

}