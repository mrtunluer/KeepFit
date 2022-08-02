package com.mertdev.weighttracking.ui.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentMeasurementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeasurementFragment : Fragment(R.layout.fragment_measurement) {

    private val binding: FragmentMeasurementBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}