package com.mertdev.weighttracking.ui.addmeasurementdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.databinding.FragmentAddMeasurementDialogBinding
import com.mertdev.weighttracking.utils.Constants.CM
import com.mertdev.weighttracking.utils.Constants.IN
import com.mertdev.weighttracking.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddMeasurementDialogFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAddMeasurementDialogBinding by viewBinding()
    private val viewModel: AddMeasurementDialogViewModel by viewModels()
    private val args: AddMeasurementDialogFragmentArgs by navArgs()

    private var lengthUnit = CM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_measurement_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val measurement = args.measurement
        initView(measurement)

        viewLifecycleOwner.lifecycleScope.launch {
            observePopBackStack()
        }

        binding.positiveBtn.setOnClickListener {
            if (binding.nameTxt.text.toString()
                    .isNotEmpty()
            ) if (measurement != null) updateMeasurement(measurement)
            else insertMeasurement()
            else requireContext().showToast("Measurement Name Cannot Be Empty")
        }
    }

    private fun initView(measurement: Measurement?) = with(binding) {
        if (measurement != null) {
            initViewForUpdate(measurement)
        }

        groupChoicesLengthUnit.setOnCheckedChangeListener { _, checkedId ->
            lengthUnit = if (checkedId == R.id.cm) CM
            else IN
        }
    }

    private fun initViewForUpdate(measurement: Measurement) = with(binding) {
        lengthUnit = measurement.lengthUnit.toString()
        titleTxt.isVisible = false
        if (lengthUnit == CM) groupChoicesLengthUnit.check(R.id.cm)
        else groupChoicesLengthUnit.check(R.id.`in`)
        nameTxt.setText(measurement.name.toString())
    }

    private fun insertMeasurement() = with(binding) {
        viewModel.insertMeasurement(
            Measurement(
                name = nameTxt.text.toString(), date = Date(), lengthUnit = lengthUnit
            )
        )
    }

    private fun updateMeasurement(measurement: Measurement) = with(binding) {
        viewModel.updateMeasurement(
            Measurement(
                id = measurement.id,
                name = nameTxt.text.toString(),
                lengthUnit = lengthUnit,
                date = measurement.date
            )
        )
    }

    private suspend fun observePopBackStack() {
        viewModel.eventFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect {
                findNavController().popBackStack()
            }
    }

}