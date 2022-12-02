package com.mertdev.keepfit.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.keepfit.data.entity.Weight
import com.mertdev.keepfit.data.repo.DataStoreRepo
import com.mertdev.keepfit.data.repo.WeightRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.readOnBoardingState.collect {
                if (it)
                    _onBoardingState.value = OnBoardingStatus.SKIP
                else
                    _onBoardingState.value = OnBoardingStatus.SHOW
            }
        }
    }

    fun addWeight(
        weight: Weight, weightUnit: String,
        heightUnit: String, targetWeight: Float,
        currentHeight: Float, gender: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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