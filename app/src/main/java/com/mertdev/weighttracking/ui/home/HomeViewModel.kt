package com.mertdev.weighttracking.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import com.mertdev.weighttracking.data.repo.WeightRepo
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.Constants.TAKE_LAST_SEVEN
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weightRepo: WeightRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<UiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<UiModel>> = _uiState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                weightRepo.getAllWeights(),
                dataStoreRepo.readAllPreferences,
                weightRepo.getMaxWeight(),
                weightRepo.getMinWeight(),
                weightRepo.getAvgWeight()
            ) { allWeights, allPreferences, maxWeight, minWeight, avgWeight ->
                _uiState.value = DataStatus.Success(
                    UiModel(
                        allWeights = allWeights,
                        lastSevenWeight = allWeights.asReversed().take(TAKE_LAST_SEVEN),
                        firstWeight = allWeights.firstOrNull()?.value,
                        currentWeight = allWeights.lastOrNull()?.value,
                        targetWeight = allPreferences.targetWeight,
                        weightUnit = allPreferences.weightUnit,
                        height = allPreferences.height,
                        heightUnit = allPreferences.heightUnit,
                        gender = allPreferences.gender,
                        maxWeight = maxWeight,
                        minWeight = minWeight,
                        avgWeight = avgWeight,
                        numberOfChartData = allPreferences.numberOfChartData,
                        isShowEmptyLayout = allWeights.isEmpty()
                    )
                )
            }.catch { exception ->
                _uiState.value = DataStatus.Error(exception.message.toString())
            }.stateIn(this)
        }
    }

    fun deleteWeight(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            weightRepo.deleteWeight(id)
        }
    }

}