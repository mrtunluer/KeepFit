package com.mertdev.keepfit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.keepfit.data.repo.DataStoreRepo
import com.mertdev.keepfit.data.repo.WeightRepo
import com.mertdev.keepfit.uimodel.WeightUiModel
import com.mertdev.keepfit.utils.Constants.TAKE_LAST_SEVEN
import com.mertdev.keepfit.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weightRepo: WeightRepo, private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<WeightUiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<WeightUiModel>> = _uiState

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
                    WeightUiModel(
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

    fun deleteWeight(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            weightRepo.deleteWeight(id)
        }
    }

}