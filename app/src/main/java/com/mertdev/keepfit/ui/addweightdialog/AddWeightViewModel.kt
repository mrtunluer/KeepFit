package com.mertdev.keepfit.ui.addweightdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.keepfit.data.entity.Weight
import com.mertdev.keepfit.data.repo.WeightRepo
import com.mertdev.keepfit.utils.enums.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddWeightViewModel @Inject constructor(
    private val weightRepo: WeightRepo
) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun getWeightByDate(startOfDay: Date, endOfDay: Date) =
        weightRepo.getWeightByDate(startOfDay, endOfDay)

    fun insertWeight(weight: Weight) {
        viewModelScope.launch(Dispatchers.IO) {
            weightRepo.insertWeight(weight)
            eventChannel.send(Event.BACK)
        }
    }

    fun updateWeight(weight: Weight) {
        viewModelScope.launch(Dispatchers.IO) {
            weightRepo.updateWeight(weight)
            eventChannel.send(Event.BACK)
        }
    }

}
