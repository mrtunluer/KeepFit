package com.mertdev.weighttracking.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import com.mertdev.weighttracking.data.repo.WeightRepo
import com.mertdev.weighttracking.utils.enums.OnBoardingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val weightRepo: WeightRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _onBoardingState = MutableStateFlow(OnBoardingStatus.LOADING)
    val onBoardingState: StateFlow<OnBoardingStatus> get() = _onBoardingState

    init {
        collectOnBoardingState()
    }

    private fun collectOnBoardingState() {
        viewModelScope.launch {
            dataStoreRepo.readOnBoardingState.collect {
                if (it)
                    _onBoardingState.value = OnBoardingStatus.SKIP
                else if (!it)
                    _onBoardingState.value = OnBoardingStatus.SHOW
            }
        }
    }

    fun addWeight(
        weight: Weight, weightUnit: String,
        heightUnit: String, targetWeight: Float,
        currentHeight: Float, gender: String
    ) {
        viewModelScope.launch {
            weightRepo.insertWeight(weight)
            dataStoreRepo.saveWeightUnit(weightUnit)
            dataStoreRepo.saveHeightUnit(heightUnit)
            dataStoreRepo.saveTargetWeight(targetWeight)
            dataStoreRepo.saveHeight(currentHeight)
            dataStoreRepo.saveGender(gender)
            dataStoreRepo.saveOnBoardingState(true)
        }
    }

}