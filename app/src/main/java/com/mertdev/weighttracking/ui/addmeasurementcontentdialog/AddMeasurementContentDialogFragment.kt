package com.mertdev.weighttracking.ui.addmeasurementcontentdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentAddMeasurementContentDialogBinding
import com.mertdev.weighttracking.databinding.FragmentAddMeasurementDialogBinding
import com.mertdev.weighttracking.ui.addmeasurementdialog.AddMeasurementDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMeasurementContentDialogFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAddMeasurementContentDialogBinding by viewBinding()
    private val viewModel: AddMeasurementContentDialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_measurement_content_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}