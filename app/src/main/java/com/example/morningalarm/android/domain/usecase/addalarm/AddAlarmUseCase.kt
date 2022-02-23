package com.example.morningalarm.android.domain.usecase.addalarm

import com.example.morningalarm.android.data.repository.AlarmRepository
import com.example.morningalarm.android.domain.model.Alarm

class AddAlarmUseCase(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(alarm : Alarm): AddAlarmUseCaseResult =
        runCatching { alarmRepository.add(alarm) }.fold(
            onSuccess = {
                AddAlarmUseCaseResult.Success(it)
            },
            onFailure = {
                AddAlarmUseCaseResult.Failure
            }
        )
}
