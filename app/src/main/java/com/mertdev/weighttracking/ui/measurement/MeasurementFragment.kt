package com.mertdev.weighttracking.ui.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentMeasurementBinding
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeasurementFragment : Fragment(R.layout.fragment_measurement) {

    private val binding: FragmentMeasurementBinding by viewBinding()
    private val viewModel: MeasurementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addBtn.setOnClickListener {
            findNavController().safeNavigate(MeasurementFragmentDirections.actionMeasurementFragmentToAddMeasurementDialogFragment())
        }

    }

    private suspend fun collectUiState() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { dataStatus ->
                when (dataStatus) {
                    is DataStatus.Loading -> onLoading()
                    is DataStatus.Error -> onError()
                    is DataStatus.Success -> dataStatus.data?.let { onSuccess(it) }
                }
            }
    }

    private fun onLoading(){
        binding.swipeRefresh.isRefreshing = true
        binding.errorTxt.isVisible = false
    }

    private fun onError(){
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = true
    }

    private fun onSuccess(data: UiModel){

    }

}