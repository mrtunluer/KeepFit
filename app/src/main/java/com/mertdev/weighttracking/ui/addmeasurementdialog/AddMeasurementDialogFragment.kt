package com.mertdev.weighttracking.ui.addmeasurementdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentAddMeasurementDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMeasurementDialogFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAddMeasurementDialogBinding by viewBinding()

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



    }

}