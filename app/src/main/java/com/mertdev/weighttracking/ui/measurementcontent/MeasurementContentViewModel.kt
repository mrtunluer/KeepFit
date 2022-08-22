package com.mertdev.weighttracking.ui.measurementcontent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import com.mertdev.weighttracking.data.repo.MeasurementRepo
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasurementContentViewModel @Inject constructor(
    private val measurementRepo: MeasurementRepo,
    private val dataStoreRepo: DataStoreRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<UiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<UiModel>> = _uiState
    private val measurementId = savedStateHandle.get<Int>("measurementId")

    init {
        fetchData(measurementId)
    }

    private fun fetchData(measurementId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementId?.let { id ->
                combine(
                    measurementRepo.getMeasurementContent(id),
                    dataStoreRepo.readAllPreferences,
                    measurementRepo.getAvgMeasurementContentValue(id),
                    measurementRepo.getMaxMeasurementContentValue(id),
                    measurementRepo.getMinMeasurementContentValue(id)
                ) { allMeasurementContent, allPreferences, avgMeasurementContentValue, maxMeasurementContentValue, minMeasurementContentValue ->
                    _uiState.value = DataStatus.Success(
                        UiModel(
                            allMeasurementContent = allMeasurementContent,
                            numberOfChartData = allPreferences.numberOfChartData,
                            maxMeasurementContentValue = maxMeasurementContentValue,
                            avgMeasurementContentValue = avgMeasurementContentValue,
                            minMeasurementContentValue = minMeasurementContentValue,
                            isShowMeasurementContentEmptyLayout = allMeasurementContent.isEmpty()
                        )
                    )
                }.catch { exception ->
                    _uiState.value = DataStatus.Error(exception.message.toString())
                }.stateIn(this)
            }
        }
    }

}