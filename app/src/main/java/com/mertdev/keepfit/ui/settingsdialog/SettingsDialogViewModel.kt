package com.mertdev.keepfit.ui.settingsdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.keepfit.data.repo.DataStoreRepo
import com.mertdev.keepfit.data.repo.MeasurementRepo
import com.mertdev.keepfit.data.repo.WeightRepo
import com.mertdev.keepfit.utils.enums.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsDialogViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val weightRepo: WeightRepo,
    private val measurementRepo: MeasurementRepo
) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun saveHeight(height: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveHeight(height)
            eventChannel.send(Event.BACK)
        }
    }

    fun saveHeightUnit(heightUnit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveHeightUnit(heightUnit)
            eventChannel.send(Event.BACK)
        }
    }

    fun saveGender(gender: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveGender(gender)
            eventChannel.send(Event.BACK)
        }
    }

    fun saveWeightUnit(weightUnit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveWeightUnit(weightUnit)
            eventChannel.send(Event.BACK)
        }
    }

    fun saveTargetWeight(targetWeight: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveTargetWeight(targetWeight)
            eventChannel.send(Event.BACK)
        }
    }

    fun saveNumberOfChartData(numberOfChartData: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveNumberOfChartData(numberOfChartData)
            eventChannel.send(Event.BACK)
        }
    }

    fun deleteWeightTable() {
        viewModelScope.launch(Dispatchers.IO) {
            weightRepo.deleteWeightTable()
            eventChannel.send(Event.BACK)
        }
    }

    fun deleteMeasurementAndMeasurementContentTable() {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.deleteMeasurementTable()
            measurementRepo.deleteMeasurementContentTable()
            eventChannel.send(Event.BACK)
        }
    }

}