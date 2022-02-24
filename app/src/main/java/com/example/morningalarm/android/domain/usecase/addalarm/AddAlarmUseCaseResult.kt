package com.example.morningalarm.android.domain.usecase.addalarm

import com.example.morningalarm.android.domain.model.Alarm

sealed class AddAlarmUseCaseResult {
    class Success(val alarmList: List<Alarm>) : AddAlarmUseCaseResult()

    object Failure : AddAlarmUseCaseResult()
}
