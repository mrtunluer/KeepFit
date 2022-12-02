package com.mertdev.keepfit.ui.addmeasurementcontentdialog

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
import com.mertdev.keepfit.R
import com.mertdev.keepfit.data.entity.MeasurementContent
import com.mertdev.keepfit.databinding.FragmentAddMeasurementContentDialogBinding
import com.mertdev.keepfit.uimodel.MeasurementUiModel
import com.mertdev.keepfit.utils.Constants
import com.mertdev.keepfit.utils.extensions.endOfDay
import com.mertdev.keepfit.utils.extensions.showDate
import com.mertdev.keepfit.utils.extensions.startOfDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddMeasurementContentDialogFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAddMeasurementContentDialogBinding by viewBinding()
    private val viewModel: AddMeasurementContentDialogViewModel by viewModels()
    private val args: AddMeasurementContentDialogFragmentArgs by navArgs()
    private var selectedDate = Date()
    private lateinit var measurementUiModel: MeasurementUiModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_measurement_content_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        measurementUiModel = args.measurementUiModel
        initView(measurementUiModel)

        viewLifecycleOwner.lifecycleScope.launch {
            observePopBackStack()
        }

        binding.positiveBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                updateOrInsert()
            }
        }

        binding.dateTxt.setOnClickListener {
            datePickerShow()
        }

    }

    private suspend fun observePopBackStack() {
        viewModel.eventFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect {
                findNavController().popBackStack()
            }
    }

    private suspend fun updateOrInsert() {
        measurementUiModel.measurementId?.let { id ->
            viewModel.getMeasurementContentByDate(
                id, selectedDate.startOfDay(), selectedDate.endOfDay()
            ).flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { measurementContent ->
                    if (measurementContent == null) insertMeasurementContent()
                    else updateMeasurementContent(measurementContent)
                }
        }
    }

    private fun insertMeasurementContent() {
        viewModel.insertMeasurementContent(
            MeasurementContent(
                measurementId = measurementUiModel.measurementId,
                value = binding.measurementContentInput.getValue(),
                date = selectedDate,
                note = binding.noteTxt.text.toString()
            )
        )
    }

    private fun updateMeasurementContent(measurementContent: MeasurementContent) {
        viewModel.updateMeasurementContent(
            MeasurementContent(
                id = measurementContent.id,
                measurementId = measurementUiModel.measurementId,
                value = binding.measurementContentInput.getValue(),
                date = selectedDate,
                note = binding.noteTxt.text.toString()
            )
        )
    }

    private fun initView(measurementUiModel: MeasurementUiModel) = with(measurementUiModel) {
        if (measurementContent == null) {
            currentMeasurementContentValue?.let {
                binding.measurementContentInput.setValue(it)
            }
        } else {
            measurementContent?.value?.let {
                binding.measurementContentInput.setValue(it)
            }
            measurementContent?.date?.let {
                selectedDate = it
            }
            measurementContent?.note?.let {
                binding.noteTxt.setText(it)
            }
        }
        binding.measurementContentNameTxt.text = measurementName.toString()
        binding.measurementContentTxt.text =
            binding.measurementContentInput.getValue().toString().plus(lengthUnit)
        lengthUnit?.let { binding.measurementContentInput.setUnitStr(it) }
        binding.measurementContentInput.setValueListener {
            binding.measurementContentTxt.text = it.toString().plus(lengthUnit)
        }
        binding.dateTxt.text = selectedDate.showDate()
    }

    private fun datePickerShow() {
        val constraintsBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.MaterialCalendarTheme).setSelection(selectedDate.time)
            .setTitleText(getString(R.string.select_date)).build()

        datePicker.addOnPositiveButtonClickListener {
            selectedDate = Date(it)
            binding.dateTxt.text = selectedDate.showDate()
        }

        datePicker.show(parentFragmentManager, Constants.EMPTY)
    }

}