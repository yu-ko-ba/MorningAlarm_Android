package com.example.morningalarm.android.domain.usecase

import com.example.morningalarm.android.data.repository.AlarmRepository
import com.example.morningalarm.android.domain.model.Alarm

class FetchAlarmListUseCase(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(): List<Alarm> {
        return alarmRepository.fetch()
    }
}
