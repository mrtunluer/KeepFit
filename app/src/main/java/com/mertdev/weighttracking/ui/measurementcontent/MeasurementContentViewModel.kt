package com.mertdev.weighttracking.ui.measurementcontent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mertdev.weighttracking.data.repo.MeasurementRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeasurementContentViewModel @Inject constructor(
    private val measurementRepo: MeasurementRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

}