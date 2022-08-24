package com.mertdev.weighttracking.ui.onboarding

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.databinding.FragmentOnBoardingBinding
import com.mertdev.weighttracking.utils.Constants.CM
import com.mertdev.weighttracking.utils.Constants.EMPTY
import com.mertdev.weighttracking.utils.Constants.FEMALE
import com.mertdev.weighttracking.utils.Constants.FT
import com.mertdev.weighttracking.utils.Constants.KG
import com.mertdev.weighttracking.utils.Constants.LB
import com.mertdev.weighttracking.utils.Constants.MALE
import com.mertdev.weighttracking.utils.extensions.initDialog
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import com.mertdev.weighttracking.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val binding: FragmentOnBoardingBinding by viewBinding()
    private val viewModel: OnBoardingViewModel by viewModels()
    private lateinit var progressBar: Dialog
    private var weightUnit = KG
    private var heightUnit = CM
    private var gender: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        selectListener()
        valueListeners()
        clickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            onBoardingState()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.heightGenderLayout.root.isVisible)
                    heightGenderLayoutVisibility(false)
                else
                    requireActivity().finish()
            }
        })

    }

    private fun clickListeners() = with(binding) {
        nextBtn.setOnClickListener {
            heightGenderLayoutVisibility(true)
        }
        heightGenderLayout.backImg.setOnClickListener {
            heightGenderLayoutVisibility(false)
        }
        heightGenderLayout.saveBtn.setOnClickListener {
            if (gender == null)
                requireContext().showToast("Please choose your gender")
            else if (currentWeightInput.getValue() == targetWeightInput.getValue())
                requireContext().showToast("Your current weight cannot be the same as your target weight")
            else
                addWeight()
        }
    }

    private fun valueListeners() = with(binding) {
        currentWeightInput.setValueListener {
            currentWeightTxt.text = it.toString().plus(weightUnit)
        }
        targetWeightInput.setValueListener {
            targetWeightTxt.text = it.toString().plus(weightUnit)
        }
        heightGenderLayout.currentHeightInput.setValueListener {
            heightGenderLayout.currentHeightTxt.text = it.toString().plus(heightUnit)
        }
    }

    private fun heightGenderLayoutVisibility(visibility: Boolean) {
        binding.heightGenderLayout.root.isVisible = visibility
    }

    private fun addWeight() {
        progressBar.show()
        val weight =
            Weight(value = binding.currentWeightInput.getValue(), date = Date(), note = EMPTY)
        val targetWeight = binding.targetWeightInput.getValue()
        val currentHeight = binding.heightGenderLayout.currentHeightInput.getValue()
        viewModel.addWeight(weight, weightUnit, heightUnit, targetWeight, currentHeight, gender!!)
    }

    private suspend fun onBoardingState() {
        viewModel.onBoardingState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { status ->
                if (status == OnBoardingStatus.SKIP) {
                    progressBar.dismiss()
                    findNavController().safeNavigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment())
                } else binding.mainLayout.isVisible = status == OnBoardingStatus.SHOW
            }
    }

    private fun initView() = with(binding) {
        progressBar = Dialog(requireContext())
        progressBar.initDialog(R.layout.custom_progress_layout)
        currentWeightTxt.text = currentWeightInput.getValue().toString().plus(weightUnit)
        targetWeightTxt.text = targetWeightInput.getValue().toString().plus(weightUnit)
        heightGenderLayout.currentHeightTxt.text =
            heightGenderLayout.currentHeightInput.getValue().toString().plus(heightUnit)
    }

    private fun selectListener() = with(binding) {
        groupChoicesWeight.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.kg)
                setWeightUnitStr(KG)
            else
                setWeightUnitStr(LB)
        }

        heightGenderLayout.groupChoicesHeight.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.cm)
                setHeightUnitStr(CM)
            else
                setHeightUnitStr(FT)
        }

        heightGenderLayout.genderCards.setOnSelectListener {
            gender = if (it.id == R.id.male_card)
                MALE
            else
                FEMALE
        }
    }

    private fun setWeightUnitStr(unitStr: String) = with(binding) {
        weightUnit = unitStr
        currentWeightInput.setUnitStr(weightUnit)
        targetWeightInput.setUnitStr(weightUnit)
        currentWeightTxt.text = currentWeightInput.getValue().toString().plus(weightUnit)
        targetWeightTxt.text = targetWeightInput.getValue().toString().plus(weightUnit)
    }

    private fun setHeightUnitStr(unitStr: String) = with(binding) {
        heightUnit = unitStr
        heightGenderLayout.currentHeightInput.setUnitStr(heightUnit)
        heightGenderLayout.currentHeightTxt.text =
            heightGenderLayout.currentHeightInput.getValue().toString().plus(heightUnit)
    }

}