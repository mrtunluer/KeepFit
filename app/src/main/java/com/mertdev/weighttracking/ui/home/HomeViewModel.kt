package com.mertdev.weighttracking.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import com.mertdev.weighttracking.data.repo.WeightRepo
import com.mertdev.weighttracking.ui.home.model.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weightRepo: WeightRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<UiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<UiModel>> = _uiState

    private val _minMaxState = MutableStateFlow<DataStatus<UiModel>>(DataStatus.Loading())
    val minMaxState: StateFlow<DataStatus<UiModel>> = _minMaxState

    init {
        fetchData()
        fetchMinMaxAvg()
    }

    private fun fetchData() {
        combine(
            weightRepo.getFirstWeight(),
            weightRepo.getCurrentWeight(),
            weightRepo.getAllWeights(),
            dataStoreRepo.readTargetWeight,
            dataStoreRepo.readWeightUnit
        ) { firstWeight, currentWeight, allWeights, targetWeight, weightUnit ->
            _uiState.value = DataStatus.Success(
                UiModel(
                    firstWeight = firstWeight.weight,
                    currentWeight = currentWeight.weight,
                    allWeights = allWeights,
                    targetWeight = targetWeight,
                    weightUnit = weightUnit
                )
            )
        }.catch { exception ->
            _uiState.value = DataStatus.Error(exception.message.toString())
        }.launchIn(viewModelScope)
    }

    private fun fetchMinMaxAvg() {
        combine(
            weightRepo.getMaxWeight(),
            weightRepo.getMinWeight(),
            weightRepo.getAvgWeight()
        ) { maxWeight, minWeight, avgWeight ->
            _minMaxState.value = DataStatus.Success(
                UiModel(
                    maxWeight = maxWeight,
                    minWeight = minWeight,
                    avgWeight = avgWeight
                )
            )
        }.catch { exception ->
            _minMaxState.value = DataStatus.Error(exception.message.toString())
        }.launchIn(viewModelScope)
    }

}