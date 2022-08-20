package com.mertdev.weighttracking.ui.addweightdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.weighttracking.data.entity.Weight
import com.mertdev.weighttracking.data.repo.WeightRepo
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

    sealed class Event {
        object PopBackStack : Event()
    }

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun getWeightByDate(startOfDay: Date, endOfDay: Date) =
        weightRepo.getWeightByDate(startOfDay, endOfDay)

    fun insertWeight(weight: Weight) {
        viewModelScope.launch(Dispatchers.IO) {
            weightRepo.insertWeight(weight)
            eventChannel.send(Event.PopBackStack)
        }
    }

    fun updateWeight(weight: Weight) {
        viewModelScope.launch(Dispatchers.IO) {
            weightRepo.updateWeight(weight)
            eventChannel.send(Event.PopBackStack)
        }
    }

}
