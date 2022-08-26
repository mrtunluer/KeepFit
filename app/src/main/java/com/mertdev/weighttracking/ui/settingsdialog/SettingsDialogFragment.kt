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
import com.mertdev.weighttracking.uimodel.WeightUiModel
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
        val weightUiModel = args.weightUiModel
        initView(weightUiModel)

        viewLifecycleOwner.lifecycleScope.launch {
            observePopBackStack()
        }

    }

    private fun initView(weightUiModel: WeightUiModel) = with(binding) {

        if (weightUiModel.gender != null) {

            titleTxt.text = getString(R.string.select_gender)
            genderCards.isVisible = true

            if (weightUiModel.gender == MALE) {
                genderCards.selectButton(R.id.male_card)
            } else {
                genderCards.selectButton(R.id.female_card)
            }

            binding.saveBtn.setOnClickListener {
                viewModel.saveGender(weightUiModel.gender!!)
            }

        } else if (weightUiModel.targetWeight != null) {

            titleTxt.text = getString(R.string.set_target_weight)
            targetWeightLayout.isVisible = true
            targetWeightTxt.text = weightUiModel.targetWeight.toString().plus(weightUiModel.weightUnit)
            targetWeightInput.setValue(weightUiModel.targetWeight!!)
            targetWeightInput.setUnitStr(weightUiModel.weightUnit!!)

            binding.saveBtn.setOnClickListener {
                viewModel.saveTargetWeight(targetWeightInput.getValue())
            }

        } else if (weightUiModel.weightUnit != null) {

            titleTxt.text = getString(R.string.select_weight_unit)
            groupChoicesWeight.isVisible = true

            if (weightUiModel.weightUnit == KG) {
                groupChoicesWeight.check(R.id.kg)
            } else {
                groupChoicesWeight.check(R.id.lb)
            }

            binding.saveBtn.setOnClickListener {
                viewModel.saveWeightUnit(weightUiModel.weightUnit!!)
            }

        } else if (weightUiModel.height != null) {

            titleTxt.text = getString(R.string.set_height)
            heightLayout.isVisible = true
            currentHeightTxt.text = weightUiModel.height.toString().plus(weightUiModel.heightUnit)
            currentHeightInput.setValue(weightUiModel.height!!)
            currentHeightInput.setUnitStr(weightUiModel.heightUnit!!)

            binding.saveBtn.setOnClickListener {
                viewModel.saveHeight(currentHeightInput.getValue())
            }

        } else if (weightUiModel.heightUnit != null) {

            titleTxt.text = getString(R.string.select_height_unit)
            groupChoicesHeight.isVisible = true

            if (weightUiModel.heightUnit == CM) {
                groupChoicesHeight.check(R.id.cm)
            } else {
                groupChoicesHeight.check(R.id.ft)
            }

            binding.saveBtn.setOnClickListener {
                viewModel.saveHeightUnit(weightUiModel.heightUnit!!)
            }

        } else if (weightUiModel.numberOfChartData != null) {

            titleTxt.text = getString(R.string.number_of_chart_data)
            groupChoicesNumberOfChartData.isVisible = true

            when (weightUiModel.numberOfChartData) {
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

            binding.saveBtn.setOnClickListener {
                viewModel.saveNumberOfChartData(weightUiModel.numberOfChartData!!)
            }

        }

        selectListener(weightUiModel)
        valueListeners(weightUiModel)

        cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun valueListeners(weightUiModel: WeightUiModel) = with(binding) {
        targetWeightInput.setValueListener {
            targetWeightTxt.text = it.toString().plus(weightUiModel.weightUnit)
        }

        currentHeightInput.setValueListener {
            currentHeightTxt.text = it.toString().plus(weightUiModel.heightUnit)
        }
    }

    private fun selectListener(weightUiModel: WeightUiModel) = with(binding) {
        genderCards.setOnSelectListener {
            weightUiModel.gender = if (it.id == R.id.male_card)
                MALE
            else
                FEMALE
        }

        groupChoicesWeight.setOnCheckedChangeListener { _, checkedId ->
            weightUiModel.weightUnit = if (checkedId == R.id.kg)
                KG
            else
                LB
        }

        groupChoicesHeight.setOnCheckedChangeListener { _, checkedId ->
            weightUiModel.heightUnit = if (checkedId == R.id.cm)
                CM
            else
                FT
        }

        groupChoicesNumberOfChartData.setOnCheckedChangeListener { _, checkedId ->
            weightUiModel.numberOfChartData = when (checkedId) {
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