package com.mertdev.weighttracking.ui.allmeasurementcontentdialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.MeasurementRepo
import com.mertdev.weighttracking.uimodel.MeasurementUiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMeasurementContentViewModel @Inject constructor(
    private val measurementRepo: MeasurementRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<MeasurementUiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<MeasurementUiModel>> = _uiState
    private val measurementUiModel = savedStateHandle.get<MeasurementUiModel>("measurementUiModel")

    init {
        fetchData(measurementUiModel?.measurementId)
    }

    private fun fetchData(measurementId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementId?.let { id ->
                measurementRepo.getMeasurementContent(id)
                    .catch { exception ->
                        _uiState.value = DataStatus.Error(exception.message.toString())
                    }.collectLatest { contentList ->
                        _uiState.value = DataStatus.Success(
                            MeasurementUiModel(
                                measurementId = id,
                                allMeasurementContent = contentList,
                                lengthUnit = measurementUiModel?.lengthUnit,
                                measurementName = measurementUiModel?.measurementName
                            )
                        )
                    }
            }
        }
    }

    fun deleteMeasurementContent(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.deleteMeasurementContent(id)
        }
    }

}