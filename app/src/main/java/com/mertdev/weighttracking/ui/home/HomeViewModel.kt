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

    private val _uiModel = MutableStateFlow<DataStatus<UiModel>>(DataStatus.Loading())
    val uiModel: StateFlow<DataStatus<UiModel>> = _uiModel

    init {
        fetchData()
    }

    private fun fetchData() {
        combine(
            weightRepo.getFirstWeight(),
            weightRepo.getCurrentWeight(),
            weightRepo.getAllWeights(),
            dataStoreRepo.readTargetWeight,
            dataStoreRepo.readWeightUnit
        ) { firstWeight, currentWeight, allWeights, targetWeight, weightUnit ->
            _uiModel.value = DataStatus.Success(
                UiModel(
                    firstWeight = firstWeight.weight,
                    currentWeight = currentWeight.weight,
                    allWeights = allWeights,
                    targetWeight = targetWeight,
                    weightUnit = weightUnit
                )
            )
        }.catch { exception ->
            _uiModel.value = DataStatus.Error(exception.message.toString())
        }.launchIn(viewModelScope)
    }

}