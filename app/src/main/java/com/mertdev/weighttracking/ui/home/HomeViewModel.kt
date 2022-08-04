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
            weightRepo.getAllWeights(),
            dataStoreRepo.readTargetWeight,
            dataStoreRepo.readWeightUnit,
            dataStoreRepo.readHeight,
            dataStoreRepo.readHeightUnit
        ) { allWeights, targetWeight, weightUnit, height, heightUnit ->
            _uiState.value = DataStatus.Success(
                UiModel(
                    allWeights = allWeights,
                    firstWeight = allWeights.firstOrNull()?.weight,
                    currentWeight = allWeights.lastOrNull()?.weight,
                    targetWeight = targetWeight,
                    weightUnit = weightUnit,
                    height = height,
                    heightUnit = heightUnit
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