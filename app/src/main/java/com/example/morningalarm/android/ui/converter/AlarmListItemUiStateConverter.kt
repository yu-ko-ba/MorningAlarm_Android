package com.example.morningalarm.android.ui.converter

import com.example.morningalarm.android.domain.model.Alarm
import com.example.morningalarm.android.ui.uistate.AlarmListItemUiState

class AlarmListItemUiStateConverter {

    fun fromAlarmList(list: List<Alarm>): List<AlarmListItemUiState> =
        list.map {
            AlarmListItemUiState(
                it.id,
                "${it.hour}:${it.minutes}"
            )
        }
}
