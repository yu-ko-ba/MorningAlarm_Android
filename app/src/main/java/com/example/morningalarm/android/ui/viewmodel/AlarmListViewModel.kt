package com.example.morningalarm.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.morningalarm.android.domain.usecase.FetchAlarmListUseCase
import com.example.morningalarm.android.ui.uistate.AlarmListItemUiState
import com.example.morningalarm.android.ui.uistate.SyncListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlarmListViewModel(private val fetchAlarmListUseCase: FetchAlarmListUseCase): ViewModel() {

    private val _syncListUiState = MutableStateFlow<SyncListUiState>(SyncListUiState.NotLoaded)
    val syncListUiState: StateFlow<SyncListUiState> = _syncListUiState

    private val _alarmListItemUiState = MutableStateFlow<List<AlarmListItemUiState>>(mutableListOf())
    val alarmListItemUiState: StateFlow<List<AlarmListItemUiState>> = _alarmListItemUiState

    fun fetchAlarmList() {
        _syncListUiState.value = SyncListUiState.Loading
        viewModelScope.launch {
            _alarmListItemUiState.value = fetchAlarmListUseCase.invoke().map {
                AlarmListItemUiState(
                    it.id,
                    "${it.hour}:${it.minutes}"
                )
            }
            _syncListUiState.value = SyncListUiState.NotLoaded
        }
    }

    class Factory(
        private val fetchAlarmListUseCase: FetchAlarmListUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass == AlarmListViewModel::class.java) {
                AlarmListViewModel(
                    fetchAlarmListUseCase
                ) as T
            } else {
                throw IllegalStateException()
            }
        }
    }
}
