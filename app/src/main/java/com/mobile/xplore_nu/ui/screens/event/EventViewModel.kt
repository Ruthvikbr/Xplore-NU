package com.mobile.xplore_nu.ui.screens.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.models.Event
import com.mobile.domain.usecases.UpcomingEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val upcomingEventsUseCase: UpcomingEventsUseCase
) : ViewModel(){

    private val _events = MutableStateFlow<List<Event>?>(null)
    val events: StateFlow<List<Event>?> = _events.asStateFlow()

    init {
        getUpcomingEvents()
    }

    fun getUpcomingEvents() {
        viewModelScope.launch {
            val eventList = upcomingEventsUseCase.invoke().data?.events
            _events.value = eventList
        }
    }
}