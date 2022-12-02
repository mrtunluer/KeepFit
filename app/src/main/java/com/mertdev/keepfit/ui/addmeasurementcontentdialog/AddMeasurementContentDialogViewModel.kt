package com.mertdev.keepfit.ui.addmeasurementcontentdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.keepfit.data.entity.MeasurementContent
import com.mertdev.keepfit.data.repo.MeasurementRepo
import com.mertdev.keepfit.utils.enums.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddMeasurementContentDialogViewModel @Inject constructor(
    private val measurementRepo: MeasurementRepo
) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun getMeasurementContentByDate(id: Int, startOfDay: Date, endOfDay: Date) =
        measurementRepo.getMeasurementContentByDate(id, startOfDay, endOfDay)

    fun insertMeasurementContent(measurementContent: MeasurementContent) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.insertMeasurementContent(measurementContent)
            eventChannel.send(Event.BACK)
        }
    }

    fun updateMeasurementContent(measurementContent: MeasurementContent) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepo.updateMeasurementContent(measurementContent)
            eventChannel.send(Event.BACK)
        }
    }

}