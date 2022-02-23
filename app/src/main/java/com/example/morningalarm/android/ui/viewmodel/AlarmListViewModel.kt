package com.example.morningalarm.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.morningalarm.android.domain.usecase.FetchAlarmListUseCase

class AlarmListViewModel(private val fetchAlarmListUseCase: FetchAlarmListUseCase): ViewModel() {

    fun fetchAlarmList() {

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
