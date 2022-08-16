package com.mertdev.weighttracking.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentSettingsBinding
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()
    private val viewModel: SettingsViewModel by viewModels()
    private var uiModel = UiModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewLifecycleOwner.lifecycleScope.launch {
            collectUiState()
        }

    }

    private fun initView() = with(binding) {
        swipeRefresh.isEnabled = false

        targetWeightCard.setOnClickListener {
            goToSettingsDialogFragment(
                UiModel(
                    weightUnit = uiModel.weightUnit,
                    targetWeight = uiModel.targetWeight
                )
            )
        }

        weightUnitCard.setOnClickListener {
            goToSettingsDialogFragment(UiModel(weightUnit = uiModel.weightUnit))
        }

        heightCard.setOnClickListener {
            goToSettingsDialogFragment(
                UiModel(
                    heightUnit = uiModel.heightUnit,
                    height = uiModel.height
                )
            )
        }

        heightUnitCard.setOnClickListener {
            goToSettingsDialogFragment(UiModel(heightUnit = uiModel.heightUnit))
        }

        genderCard.setOnClickListener {
            goToSettingsDialogFragment(UiModel(gender = uiModel.gender))
        }

        numberOfChartDataCard.setOnClickListener {
            goToSettingsDialogFragment(UiModel(numberOfChartData = uiModel.numberOfChartData))
        }

    }

    private fun goToSettingsDialogFragment(uiModel: UiModel) {
        findNavController().safeNavigate(
            SettingsFragmentDirections.actionSettingsFragmentToSettingsDialogFragment(uiModel)
        )
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
        binding.genderTxt.text = data.gender
        binding.weightUnitTxt.text = data.weightUnit
        binding.heightUnitTxt.text = data.heightUnit
        binding.heightTxt.text = data.height.toString()
        binding.targetWeightTxt.text = data.targetWeight.toString()
        binding.numberOfChartDataTxt.text = data.numberOfChartData.toString()
        uiModel = this
    }

}