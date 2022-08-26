package com.mertdev.weighttracking.ui.measurementcontent

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.databinding.FragmentMeasurementContentBinding
import com.mertdev.weighttracking.uimodel.MeasurementUiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.round
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeasurementContentFragment : Fragment(R.layout.fragment_measurement_content) {

    private val binding: FragmentMeasurementContentBinding by viewBinding()
    private val viewModel: MeasurementContentViewModel by viewModels()
    private val args: MeasurementContentFragmentArgs by navArgs()
    private var measurementUiModel = MeasurementUiModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val measurement = args.measurement
        initView(measurement)

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

        binding.backImg.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addBtn.setOnClickListener {
            goToAddMeasurementContentDialogFragment(measurementUiModel)
        }

    }

    private fun initView(measurement: Measurement) = with(binding) {
        swipeRefresh.isEnabled = false
        titleTxt.text = measurement.name
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

    private fun onLoadingForUiState() = with(binding) {
        errorTxt.isVisible = false
        swipeRefresh.isRefreshing = true
    }

    private fun onErrorForUiState() = with(binding) {
        errorTxt.isVisible = true
        swipeRefresh.isRefreshing = false
    }

    private fun onSuccessForUiState(data: MeasurementUiModel) = with(data) {
        binding.errorTxt.isVisible = false
        binding.swipeRefresh.isRefreshing = false
        binding.currentValueTxt.text = currentMeasurementContentValue.toString()
        binding.lengthUnitTxt.text = lengthUnit.toString()
        binding.maxValueTxt.text = maxMeasurementContentValue.toString()
        binding.minValueTxt.text = minMeasurementContentValue.toString()
        binding.avgValueTxt.text = avgMeasurementContentValue?.round(1).toString()
        emptyLayoutState(this)
        measurementUiModel = this
    }

    private fun emptyLayoutState(measurementUiModel: MeasurementUiModel) =
        with(binding.emptyLayout) {
            root.isVisible = measurementUiModel.isShowEmptyLayout == true
            addBtn.setOnClickListener {
                goToAddMeasurementContentDialogFragment(measurementUiModel)
            }
        }

    private fun goToAddMeasurementContentDialogFragment(measurementUiModel: MeasurementUiModel) {
        findNavController().safeNavigate(
            MeasurementContentFragmentDirections.actionMeasurementContentFragmentToAddMeasurementContentDialogFragment(
                measurementUiModel
            )
        )
    }


}