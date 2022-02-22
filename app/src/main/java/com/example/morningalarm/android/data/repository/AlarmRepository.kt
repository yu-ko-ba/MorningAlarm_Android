package com.example.morningalarm.android.data.repository

import com.example.morningalarm.android.data.datasource.AlarmDataSource
import com.example.morningalarm.android.domain.model.Alarm

class AlarmRepository(private val dataSource: AlarmDataSource) {

    suspend fun fetch(): List<Alarm> = dataSource.fetch()
}
