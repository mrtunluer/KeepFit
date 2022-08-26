package com.mertdev.weighttracking.ui.measurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.MeasurementRepo
import com.mertdev.weighttracking.uimodel.MeasurementUiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasurementViewModel @Inject constructor(
    private val measurementRepo: MeasurementRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<MeasurementUiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<MeasurementUiModel>> = _uiState

    init {
        getAllMeasurements()
    }

    private fun getAllMeasurements() {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.getAllMeasurements()
                .catch { exception ->
                    _uiState.value = DataStatus.Error(exception.message.toString())
                }.collectLatest { allMeasurements ->
                    _uiState.value = DataStatus.Success(
                        MeasurementUiModel(
                            allMeasurements = allMeasurements,
                            isShowEmptyLayout = allMeasurements.isEmpty()
                        )
                    )
                }
        }
    }

    fun deleteMeasurement(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.deleteMeasurement(id)
        }
    }

}