package com.example.morningalarm.android.domain.usecase.fetchalarmlist

import com.example.morningalarm.android.data.repository.AlarmRepository
import com.example.morningalarm.android.domain.model.Alarm

class FetchAlarmListUseCase(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(): FetchAlarmListUseCaseResult =
        runCatching { alarmRepository.fetch() }.fold(
            onSuccess = {
                FetchAlarmListUseCaseResult.Success(it)
            },
            onFailure = {
                FetchAlarmListUseCaseResult.Failure
            }
        )
}
