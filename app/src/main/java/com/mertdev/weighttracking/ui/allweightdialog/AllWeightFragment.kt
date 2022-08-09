package com.mertdev.weighttracking.ui.allweightdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mertdev.weighttracking.R
import com.mertdev.weighttracking.databinding.FragmentAllWeightBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllWeightFragment : BottomSheetDialogFragment() {

    private val binding: FragmentAllWeightBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_all_weight, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}