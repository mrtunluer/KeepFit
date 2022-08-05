package com.mertdev.weighttracking.ui.adddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.databinding.FragmentAddWeightBinding
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.Constants.EMPTY
import com.mertdev.weighttracking.utils.extensions.endOfDay
import com.mertdev.weighttracking.utils.extensions.showDate
import com.mertdev.weighttracking.utils.extensions.startOfDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddWeightFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAddWeightBinding by viewBinding()
    private val viewModel: AddWeightViewModel by viewModels()
    private val args: AddWeightFragmentArgs by navArgs()
    private var selectedDate = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_weight, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uiModel = args.uiModel
        initView(uiModel)

        binding.saveBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                updateOrInsert()
            }
        }

        binding.dateTxt.setOnClickListener {
            datePickerShow()
        }

    }

    private suspend fun updateOrInsert() {
        viewModel.getWeightByDate(selectedDate.startOfDay(), selectedDate.endOfDay())
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { weight ->
                if (weight == null)
                    insertWeight()
                else
                    updateWeight(weight)
            }
    }

    private fun insertWeight() {
        viewModel.insertWeight(
            Weight(
                value = binding.weightInput.getValue(),
                date = selectedDate,
                note = binding.noteTxt.text.toString()
            )
        )
        findNavController().popBackStack()
    }


    private fun updateWeight(weight: Weight) {
        viewModel.updateWeight(
            Weight(
                weight.id,
                binding.weightInput.getValue(),
                selectedDate,
                binding.noteTxt.text.toString()
            )
        )
        findNavController().popBackStack()
    }

    private fun datePickerShow() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.MaterialCalendarTheme)
            .setSelection(selectedDate.time)
            .setTitleText(getString(R.string.select_date))
            .build()

        datePicker.addOnPositiveButtonClickListener {
            selectedDate = Date(it)
            binding.dateTxt.text = selectedDate.showDate()
        }

        datePicker.show(parentFragmentManager, EMPTY)
    }


    private fun initView(uiModel: UiModel) = with(uiModel) {
        if (weight == null) {
            currentWeight?.let {
                binding.weightInput.setValue(it)
                binding.weightTxt.text = it.toString().plus(weightUnit)
            }
        } else {
            weight?.value?.let {
                binding.weightInput.setValue(it)
                binding.weightTxt.text = it.toString().plus(weightUnit)
            }
            weight?.date?.let {
                selectedDate = it
            }
        }
        weightUnit?.let { binding.weightInput.setUnitStr(it) }
        binding.weightInput.setValueListener {
            binding.weightTxt.text = it.toString().plus(weightUnit)
        }
        binding.dateTxt.text = selectedDate.showDate()
    }

}