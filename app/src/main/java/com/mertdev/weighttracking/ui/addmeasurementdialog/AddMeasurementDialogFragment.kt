package com.mertdev.weighttracking.ui.addmeasurementdialog

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

    private var lengthUnit = CM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_measurement_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewLifecycleOwner.lifecycleScope.launch {
            observePopBackStack()
        }
    }

    private fun initView() = with(binding){
        groupChoicesLengthUnit.setOnCheckedChangeListener { _, checkedId ->
            lengthUnit = if (checkedId == R.id.cm)
                CM
            else
                IN
        }

        saveBtn.setOnClickListener {
            if(nameTxt.text.toString().isNotEmpty())
                insertMeasurement()
            else
                requireContext().showToast("Measurement Name Cannot Be Empty")
        }
    }

    private fun insertMeasurement() = with(binding){
        viewModel.insertMeasurement(
            Measurement(
                name = nameTxt.text.toString(),
                date = Date(),
                lengthUnit = lengthUnit
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