package com.mertdev.keepfit.ui.settings

import android.content.Intent
import android.net.Uri
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
import com.mertdev.keepfit.BuildConfig
import com.mertdev.keepfit.R
import com.mertdev.keepfit.databinding.FragmentSettingsBinding
import com.mertdev.keepfit.uimodel.SettingsUiModel
import com.mertdev.keepfit.utils.Constants.MALE
import com.mertdev.keepfit.utils.enums.DataStatus
import com.mertdev.keepfit.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()
    private val viewModel: SettingsViewModel by viewModels()
    private var settingsUiModel = SettingsUiModel()

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
                SettingsUiModel(
                    weightUnit = settingsUiModel.weightUnit,
                    targetWeight = settingsUiModel.targetWeight
                )
            )
        }

        weightUnitCard.setOnClickListener {
            goToSettingsDialogFragment(SettingsUiModel(weightUnit = settingsUiModel.weightUnit))
        }

        heightCard.setOnClickListener {
            goToSettingsDialogFragment(
                SettingsUiModel(
                    heightUnit = settingsUiModel.heightUnit, height = settingsUiModel.height
                )
            )
        }

        heightUnitCard.setOnClickListener {
            goToSettingsDialogFragment(SettingsUiModel(heightUnit = settingsUiModel.heightUnit))
        }

        genderCard.setOnClickListener {
            goToSettingsDialogFragment(SettingsUiModel(gender = settingsUiModel.gender))
        }

        numberOfChartDataCard.setOnClickListener {
            goToSettingsDialogFragment(SettingsUiModel(numberOfChartData = settingsUiModel.numberOfChartData))
        }

        deleteWeightDataCard.setOnClickListener {
            goToSettingsDialogFragment(SettingsUiModel(isDeleteAllWeightData = true))
        }

        deleteMeasurementDataCard.setOnClickListener {
            goToSettingsDialogFragment(SettingsUiModel(isDeleteAllMeasurementData = true))
        }

        shareCard.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.type = "text/plain"
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
            )
            startActivity(Intent.createChooser(sendIntent, "Choose One"))
        }

        rateUsCard.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        }

    }

    private fun goToSettingsDialogFragment(settingsUiModel: SettingsUiModel) {
        findNavController().safeNavigate(
            SettingsFragmentDirections.actionSettingsFragmentToSettingsDialogFragment(
                settingsUiModel
            )
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

    private fun onSuccessForUiState(data: SettingsUiModel) = with(data) {
        binding.swipeRefresh.isRefreshing = false
        binding.errorTxt.isVisible = false
        binding.weightUnitTxt.text = weightUnit
        binding.heightUnitTxt.text = heightUnit
        binding.heightTxt.text = height.toString()
        binding.targetWeightTxt.text = targetWeight.toString()
        binding.numberOfChartDataTxt.text = numberOfChartData.toString()
        setGenderImg(gender)
        settingsUiModel = this
    }

    private fun setGenderImg(gender: String?) = with(binding) {
        if (gender == MALE) genderImg.setImageResource(R.drawable.male)
        else genderImg.setImageResource(R.drawable.female)
    }

}