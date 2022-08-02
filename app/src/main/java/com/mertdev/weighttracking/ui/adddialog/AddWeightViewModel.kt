package com.mertdev.weighttracking.ui.adddialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.data.repo.WeightRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddWeightViewModel @Inject constructor(
    private val weightRepo: WeightRepo
) : ViewModel() {

    fun getWeightByDate(startOfDay: Date, endOfDay: Date) =
        weightRepo.getWeightByDate(startOfDay, endOfDay)


    fun insertWeight(weight: Weight) {
        viewModelScope.launch {
            weightRepo.insertWeight(weight)
        }
    }

    fun updateWeight(weight: Weight) {
        viewModelScope.launch {
            weightRepo.updateWeight(weight)
        }
    }

}
