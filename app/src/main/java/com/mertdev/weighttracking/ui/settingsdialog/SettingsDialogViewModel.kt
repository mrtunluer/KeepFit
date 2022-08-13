package com.mertdev.weighttracking.ui.settingsdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsDialogViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    sealed class Event {
        object PopBackStack : Event()
    }

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun saveHeight(height: Float){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveHeight(height)
            eventChannel.send(Event.PopBackStack)
        }
    }

    fun saveHeightUnit(heightUnit: String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveHeightUnit(heightUnit)
            eventChannel.send(Event.PopBackStack)
        }
    }

    fun saveGender(gender: String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveGender(gender)
            eventChannel.send(Event.PopBackStack)
        }
    }

    fun saveWeightUnit(weightUnit: String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveWeightUnit(weightUnit)
            eventChannel.send(Event.PopBackStack)
        }
    }

    fun saveTargetWeight(targetWeight: Float){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveTargetWeight(targetWeight)
            eventChannel.send(Event.PopBackStack)
        }
    }

}