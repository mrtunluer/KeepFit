package com.mertdev.weighttracking.ui.measurementcontent

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentMeasurementContentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeasurementContentFragment : Fragment(R.layout.fragment_measurement_content) {

    private val binding: FragmentMeasurementContentBinding by viewBinding()
    private val viewModel: MeasurementContentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}