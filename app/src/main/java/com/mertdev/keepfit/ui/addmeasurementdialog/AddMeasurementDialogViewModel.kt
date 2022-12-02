package com.mertdev.keepfit.ui.addmeasurementdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.keepfit.data.entity.Measurement
import com.mertdev.keepfit.data.repo.MeasurementRepo
import com.mertdev.keepfit.utils.enums.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMeasurementDialogViewModel @Inject constructor(
    private val measurementRepo: MeasurementRepo
) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun insertMeasurement(measurement: Measurement) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.insertMeasurement(measurement)
            eventChannel.send(Event.BACK)
        }
    }

    fun updateMeasurement(measurement: Measurement) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.updateMeasurement(measurement)
            eventChannel.send(Event.BACK)
        }
    }

}