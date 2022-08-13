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
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.Constants.CM
import com.mertdev.weighttracking.utils.Constants.FEMALE
import com.mertdev.weighttracking.utils.Constants.FT
import com.mertdev.weighttracking.utils.Constants.KG
import com.mertdev.weighttracking.utils.Constants.LB
import com.mertdev.weighttracking.utils.Constants.MALE
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
        val uiModel = args.uiModel
        initView(uiModel)

        viewLifecycleOwner.lifecycleScope.launch {
            observePopBackStack()
        }

    }

    private fun initView(uiModel: UiModel) = with(binding) {

        if (uiModel.gender != null) {

            titleTxt.text = getString(R.string.select_gender)
            genderCards.isVisible = true

            if (uiModel.gender == MALE) {
                genderCards.selectButton(R.id.male_card)
            } else {
                genderCards.selectButton(R.id.female_card)
            }

            binding.saveBtn.setOnClickListener {
                viewModel.saveGender(uiModel.gender!!)
            }

        } else if (uiModel.weightUnit != null && uiModel.targetWeight != null) {

            titleTxt.text = getString(R.string.set_target_weight)
            targetWeightLayout.isVisible = true
            targetWeightTxt.text = uiModel.targetWeight.toString().plus(uiModel.weightUnit)
            targetWeightInput.setValue(uiModel.targetWeight!!)
            targetWeightInput.setUnitStr(uiModel.weightUnit!!)

            binding.saveBtn.setOnClickListener {
                viewModel.saveTargetWeight(targetWeightInput.getValue())
            }

        } else if (uiModel.weightUnit != null) {

            titleTxt.text = getString(R.string.select_weight_unit)
            groupChoicesWeight.isVisible = true

            if (uiModel.weightUnit == KG) {
                groupChoicesWeight.check(R.id.kg)
            } else {
                groupChoicesWeight.check(R.id.lb)
            }

            binding.saveBtn.setOnClickListener {
                viewModel.saveWeightUnit(uiModel.weightUnit!!)
            }

        } else if (uiModel.heightUnit != null && uiModel.height != null) {

            titleTxt.text = getString(R.string.set_height)
            heightLayout.isVisible = true
            currentHeightTxt.text = uiModel.height.toString().plus(uiModel.heightUnit)
            currentHeightInput.setValue(uiModel.height!!)
            currentHeightInput.setUnitStr(uiModel.heightUnit!!)

            binding.saveBtn.setOnClickListener {
                viewModel.saveHeight(currentHeightInput.getValue())
            }

        } else if (uiModel.heightUnit != null) {

            titleTxt.text = getString(R.string.select_height_unit)
            groupChoicesHeight.isVisible = true

            if (uiModel.heightUnit == CM) {
                groupChoicesHeight.check(R.id.cm)
            } else {
                groupChoicesHeight.check(R.id.ft)
            }

            binding.saveBtn.setOnClickListener {
                viewModel.saveHeightUnit(uiModel.heightUnit!!)
            }

        }

        genderCards.setOnSelectListener {
            uiModel.gender = if (it.id == R.id.male_card)
                MALE
            else
                FEMALE
        }

        groupChoicesWeight.setOnCheckedChangeListener { _, checkedId ->
            uiModel.weightUnit = if (checkedId == R.id.kg)
                KG
            else
                LB
        }

        groupChoicesHeight.setOnCheckedChangeListener { _, checkedId ->
            uiModel.heightUnit = if (checkedId == R.id.cm)
                CM
            else
                FT
        }

        targetWeightInput.setValueListener {
            targetWeightTxt.text = it.toString().plus(uiModel.weightUnit)
        }

        currentHeightInput.setValueListener {
            currentHeightTxt.text = it.toString().plus(uiModel.heightUnit)
        }

        cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }


    private suspend fun observePopBackStack() {
        viewModel.eventFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect {
                findNavController().popBackStack()
            }
    }

}