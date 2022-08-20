package com.mertdev.weighttracking.ui.measurement

import androidx.lifecycle.ViewModel
import com.mertdev.weighttracking.data.repo.MeasurementRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeasurementViewModel @Inject constructor(
    private val measurementRepo: MeasurementRepo
) : ViewModel() {

}