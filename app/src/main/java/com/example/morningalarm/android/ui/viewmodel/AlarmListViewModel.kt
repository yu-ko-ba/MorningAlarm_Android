package com.example.morningalarm.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.morningalarm.android.domain.model.Alarm
import com.example.morningalarm.android.domain.usecase.addalarm.AddAlarmUseCase
import com.example.morningalarm.android.domain.usecase.addalarm.AddAlarmUseCaseResult
import com.example.morningalarm.android.domain.usecase.fetchalarmlist.FetchAlarmListUseCase
import com.example.morningalarm.android.domain.usecase.fetchalarmlist.FetchAlarmListUseCaseResult
import com.example.morningalarm.android.ui.converter.AlarmListItemUiStateConverter
import com.example.morningalarm.android.ui.uistate.AlarmListItemUiState
import com.example.morningalarm.android.ui.uistate.SyncListUiState
import com.example.morningalarm.android.ui.uistate.TimePickerInputUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class AlarmListViewModel(
    private val fetchAlarmListUseCase: FetchAlarmListUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val alarmListItemUiStateConverter: AlarmListItemUiStateConverter = AlarmListItemUiStateConverter()
): ViewModel() {

    private val _syncListUiState = MutableStateFlow<SyncListUiState>(SyncListUiState.NotLoaded)
    val syncListUiState: StateFlow<SyncListUiState> = _syncListUiState

    private val _alarmListItemUiState = MutableStateFlow<List<AlarmListItemUiState>>(mutableListOf())
    val alarmListItemUiState: StateFlow<List<AlarmListItemUiState>> = _alarmListItemUiState

    fun fetchAlarmList() {
        _syncListUiState.value = SyncListUiState.Loading
        viewModelScope.launch {
            val result = fetchAlarmListUseCase.invoke()
            if(result is FetchAlarmListUseCaseResult.Success){
                _alarmListItemUiState.value = alarmListItemUiStateConverter.fromAlarmList(result.alarmList)
                _syncListUiState.value = SyncListUiState.Success
            } else {
                _syncListUiState.value = SyncListUiState.Failure
            }
            _syncListUiState.value = SyncListUiState.NotLoaded
        }
    }

    fun addAlarm(input: TimePickerInputUiState){
        val newAlarm = Alarm(
            UUID.randomUUID().toString(),
            input.hourOfDay,
            input.minute
        )
        val newItem = alarmListItemUiStateConverter.fromAlarm(newAlarm).also { it.isSynchronized = false }
        val list = ArrayList(_alarmListItemUiState.value)
        list.add(newItem)
        _alarmListItemUiState.value = list
        viewModelScope.launch {
            val result = addAlarmUseCase.invoke(newAlarm)
            if(result is AddAlarmUseCaseResult.Success){
                _alarmListItemUiState.value = alarmListItemUiStateConverter.fromAlarmList(result.alarmList)
            }
        }
    }

    class Factory(
        private val fetchAlarmListUseCase: FetchAlarmListUseCase,
        private val addAlarmUseCase: AddAlarmUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass == AlarmListViewModel::class.java) {
                AlarmListViewModel(
                    fetchAlarmListUseCase,
                    addAlarmUseCase
                ) as T
            } else {
                throw IllegalStateException()
            }
        }
    }
}
