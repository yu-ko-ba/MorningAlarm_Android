package com.example.morningalarm.android.domain.usecase.fetchalarmlist

import com.example.morningalarm.android.domain.model.Alarm

sealed class FetchAlarmListUseCaseResult {
    class Success(val alarmList: List<Alarm>) : FetchAlarmListUseCaseResult()

    object Failure : FetchAlarmListUseCaseResult()
}
