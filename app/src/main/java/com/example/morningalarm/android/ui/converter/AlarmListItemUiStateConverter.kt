package com.example.morningalarm.android.ui.converter

import com.example.morningalarm.android.domain.model.Alarm
import com.example.morningalarm.android.ui.uistate.AlarmListItemUiState

class AlarmListItemUiStateConverter {

    fun fromAlarm(alarm: Alarm): AlarmListItemUiState =
        AlarmListItemUiState(
            alarm.id,
            "${formatDigit(alarm.hour)}:${formatDigit(alarm.minutes)}"
        )

    fun fromAlarmList(list: List<Alarm>): List<AlarmListItemUiState> =
        list.map {
            AlarmListItemUiState(
                it.id,
                "${formatDigit(it.hour)}:${formatDigit(it.minutes)}"
            )
        }

    private fun formatDigit(number : Int) = "%0,2d".format(number)
}
