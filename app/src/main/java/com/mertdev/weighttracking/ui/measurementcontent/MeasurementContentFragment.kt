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
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeasurementContentFragment : Fragment(R.layout.fragment_measurement_content) {

    private val binding: FragmentMeasurementContentBinding by viewBinding()
    private val viewModel: MeasurementContentViewModel by viewModels()
    private val args: MeasurementContentFragmentArgs by navArgs()

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

    private fun onSuccessForUiState(data: UiModel) = with(data) {
        binding.errorTxt.isVisible = false
        binding.swipeRefresh.isRefreshing = false
        emptyLayoutState(this)
    }

    private fun emptyLayoutState(uiModel: UiModel) = with(binding.emptyLayout) {
        root.isVisible = uiModel.isShowEmptyLayout == true
        addBtn.setOnClickListener {
            findNavController().navigate(MeasurementContentFragmentDirections.actionMeasurementContentFragmentToAddMeasurementContentDialogFragment())
        }
    }

}