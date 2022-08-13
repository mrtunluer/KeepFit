package com.mertdev.weighttracking.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import com.mertdev.weighttracking.uimodel.UiModel
import com.mertdev.weighttracking.utils.enums.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<UiModel>>(DataStatus.Loading())
    val uiState: StateFlow<DataStatus<UiModel>> = _uiState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.readAllPreferences
                .catch { exception ->
                    _uiState.value = DataStatus.Error(exception.message.toString())
                }.collect {
                    _uiState.value = DataStatus.Success(
                        UiModel(
                            targetWeight = it.targetWeight,
                            weightUnit = it.weightUnit,
                            height = it.height,
                            heightUnit = it.heightUnit,
                            gender = it.gender
                        )
                    )
                }
        }
    }

}