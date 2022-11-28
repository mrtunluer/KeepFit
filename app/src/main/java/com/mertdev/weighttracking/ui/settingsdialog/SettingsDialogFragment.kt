package com.mertdev.weighttracking.ui.settingsdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentSettingsDialogBinding
import com.mertdev.weighttracking.uimodel.SettingsUiModel
import com.mertdev.weighttracking.utils.Constants.CM
import com.mertdev.weighttracking.utils.Constants.FEMALE
import com.mertdev.weighttracking.utils.Constants.FT
import com.mertdev.weighttracking.utils.Constants.KG
import com.mertdev.weighttracking.utils.Constants.LAST_HUNDRED_EIGHTY
import com.mertdev.weighttracking.utils.Constants.LAST_NINETY
import com.mertdev.weighttracking.utils.Constants.LAST_THIRTY
import com.mertdev.weighttracking.utils.Constants.LB
import com.mertdev.weighttracking.utils.Constants.MALE
import com.mertdev.weighttracking.utils.Constants.NUMBER_OF_INITIAL_DATA_IN_CHART
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsDialogFragment : DialogFragment(R.layout.fragment_settings_dialog) {

    private val binding: FragmentSettingsDialogBinding by viewBinding()
    private val args: SettingsDialogFragmentArgs by navArgs()
    private val viewModel: SettingsDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val settingsUiModel = args.settingsUiModel
        initView(settingsUiModel)

        viewLifecycleOwner.lifecycleScope.launch {
            observePopBackStack()
        }

    }

    private fun initView(settingsUiModel: SettingsUiModel) = with(binding) {

        if (settingsUiModel.gender != null) {

            titleTxt.text = getString(R.string.select_gender)
            genderCards.isVisible = true

            if (settingsUiModel.gender == MALE) {
                genderCards.selectButton(R.id.male_card)
            } else {
                genderCards.selectButton(R.id.female_card)
            }

            positiveBtn.setOnClickListener {
                viewModel.saveGender(settingsUiModel.gender!!)
            }

        } else if (settingsUiModel.targetWeight != null) {

            titleTxt.text = getString(R.string.set_target_weight)
            targetWeightLayout.isVisible = true
            targetWeightTxt.text =
                settingsUiModel.targetWeight.toString().plus(settingsUiModel.weightUnit)
            targetWeightInput.setValue(settingsUiModel.targetWeight!!)
            targetWeightInput.setUnitStr(settingsUiModel.weightUnit!!)

            positiveBtn.setOnClickListener {
                viewModel.saveTargetWeight(targetWeightInput.getValue())
            }

        } else if (settingsUiModel.weightUnit != null) {

            titleTxt.text = getString(R.string.select_weight_unit)
            groupChoicesWeight.isVisible = true

            if (settingsUiModel.weightUnit == KG) {
                groupChoicesWeight.check(R.id.kg)
            } else {
                groupChoicesWeight.check(R.id.lb)
            }

            positiveBtn.setOnClickListener {
                viewModel.saveWeightUnit(settingsUiModel.weightUnit!!)
            }

        } else if (settingsUiModel.height != null) {

            titleTxt.text = getString(R.string.set_height)
            heightLayout.isVisible = true
            currentHeightTxt.text =
                settingsUiModel.height.toString().plus(settingsUiModel.heightUnit)
            currentHeightInput.setValue(settingsUiModel.height!!)
            currentHeightInput.setUnitStr(settingsUiModel.heightUnit!!)

            positiveBtn.setOnClickListener {
                viewModel.saveHeight(currentHeightInput.getValue())
            }

        } else if (settingsUiModel.heightUnit != null) {

            titleTxt.text = getString(R.string.select_height_unit)
            groupChoicesHeight.isVisible = true

            if (settingsUiModel.heightUnit == CM) {
                groupChoicesHeight.check(R.id.cm)
            } else {
                groupChoicesHeight.check(R.id.ft)
            }

            positiveBtn.setOnClickListener {
                viewModel.saveHeightUnit(settingsUiModel.heightUnit!!)
            }

        } else if (settingsUiModel.numberOfChartData != null) {

            titleTxt.text = getString(R.string.number_of_chart_data)
            groupChoicesNumberOfChartData.isVisible = true

            when (settingsUiModel.numberOfChartData) {
                NUMBER_OF_INITIAL_DATA_IN_CHART -> {
                    groupChoicesNumberOfChartData.check(R.id.last_fifteen)
                }
                LAST_THIRTY -> {
                    groupChoicesNumberOfChartData.check(R.id.last_thirty)
                }
                LAST_NINETY -> {
                    groupChoicesNumberOfChartData.check(R.id.last_ninety)
                }
                LAST_HUNDRED_EIGHTY -> {
                    groupChoicesNumberOfChartData.check(R.id.last_hundred_eighty)
                }
            }

            positiveBtn.setOnClickListener {
                viewModel.saveNumberOfChartData(settingsUiModel.numberOfChartData!!)
            }

        } else if (settingsUiModel.isDeleteAllWeightData != null) {

            positiveBtn.text = getString(R.string.yes)
            negativeBtn.text = getString(R.string.no)
            titleTxt.text = getString(R.string.are_you_sure_delete_all_weight_data)

            positiveBtn.setOnClickListener {
                viewModel.deleteWeightTable()
            }

        } else if (settingsUiModel.isDeleteAllMeasurementData != null) {

            positiveBtn.text = getString(R.string.yes)
            negativeBtn.text = getString(R.string.no)
            titleTxt.text = getString(R.string.are_you_sure_delete_all_measurement_data)

            positiveBtn.setOnClickListener {
                viewModel.deleteMeasurementAndMeasurementContentTable()
            }

        }

        selectListener(settingsUiModel)
        valueListeners(settingsUiModel)

        negativeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun valueListeners(settingsUiModel: SettingsUiModel) = with(binding) {
        targetWeightInput.setValueListener {
            targetWeightTxt.text = it.toString().plus(settingsUiModel.weightUnit)
        }

        currentHeightInput.setValueListener {
            currentHeightTxt.text = it.toString().plus(settingsUiModel.heightUnit)
        }
    }

    private fun selectListener(settingsUiModel: SettingsUiModel) = with(binding) {
        genderCards.setOnSelectListener {
            settingsUiModel.gender = if (it.id == R.id.male_card) MALE
            else FEMALE
        }

        groupChoicesWeight.setOnCheckedChangeListener { _, checkedId ->
            settingsUiModel.weightUnit = if (checkedId == R.id.kg) KG
            else LB
        }

        groupChoicesHeight.setOnCheckedChangeListener { _, checkedId ->
            settingsUiModel.heightUnit = if (checkedId == R.id.cm) CM
            else FT
        }

        groupChoicesNumberOfChartData.setOnCheckedChangeListener { _, checkedId ->
            settingsUiModel.numberOfChartData = when (checkedId) {
                R.id.last_fifteen -> NUMBER_OF_INITIAL_DATA_IN_CHART
                R.id.last_thirty -> LAST_THIRTY
                R.id.last_ninety -> LAST_NINETY
                else -> LAST_HUNDRED_EIGHTY
            }
        }
    }

    private suspend fun observePopBackStack() {
        viewModel.eventFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect {
                findNavController().popBackStack()
            }
    }

}