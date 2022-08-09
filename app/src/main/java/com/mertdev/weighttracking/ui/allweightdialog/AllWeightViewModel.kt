package com.mertdev.weighttracking.ui.allweightdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import com.mertdev.weighttracking.data.repo.WeightRepo
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllWeightViewModel @Inject constructor(
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
            ) { allWeights, allPreferences ->
                _uiState.value = DataStatus.Success(
                    UiModel(
                        allWeights = allWeights,
                        weightUnit = allPreferences.weightUnit
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