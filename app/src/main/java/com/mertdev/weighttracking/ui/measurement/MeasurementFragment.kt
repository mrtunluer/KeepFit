package com.mertdev.weighttracking.ui.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.navigation.fragment.findNavController
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentMeasurementBinding
import com.mertdev.weighttracking.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeasurementFragment : Fragment(R.layout.fragment_measurement) {
    private val binding: FragmentMeasurementBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addBtn.setOnClickListener {
            findNavController().safeNavigate(MeasurementFragmentDirections.actionMeasurementFragmentToAddMeasurementDialogFragment())
        }

    }

}