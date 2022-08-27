package com.mertdev.weighttracking.ui.measurementcontent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.entity.Measurement
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import com.mertdev.weighttracking.data.repo.MeasurementRepo
import com.mertdev.weighttracking.uimodel.MeasurementUiModel
import com.mertdev.weighttracking.utils.Constants.TAKE_LAST_SEVEN
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

    private val _uiState = MutableStateFlow<DataStatus<MeasurementUiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<MeasurementUiModel>> = _uiState
    private val measurement = savedStateHandle.get<Measurement>("measurement")

    init {
        fetchData(measurement?.id)
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
                        MeasurementUiModel(
                            measurementId = id,
                            measurementName = measurement?.name,
                            allMeasurementContent = allMeasurementContent,
                            lastSevenMeasurementContent = allMeasurementContent.asReversed()
                                .take(TAKE_LAST_SEVEN),
                            numberOfChartData = allPreferences.numberOfChartData,
                            maxMeasurementContentValue = maxMeasurementContentValue,
                            avgMeasurementContentValue = avgMeasurementContentValue,
                            minMeasurementContentValue = minMeasurementContentValue,
                            isShowEmptyLayout = allMeasurementContent.isEmpty(),
                            lengthUnit = measurement?.lengthUnit,
                            currentMeasurementContentValue = allMeasurementContent.lastOrNull()?.value,
                        )
                    )
                }.catch { exception ->
                    _uiState.value = DataStatus.Error(exception.message.toString())
                }.stateIn(this)
            }
        }
    }

    fun deleteMeasurementContent(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.deleteMeasurementContent(id)
        }
    }

}