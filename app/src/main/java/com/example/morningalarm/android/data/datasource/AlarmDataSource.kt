package com.example.morningalarm.android.data.datasource

import com.example.morningalarm.android.domain.model.Alarm

interface AlarmDataSource {
    suspend fun fetch(): List<Alarm>

    suspend fun add(alarm: Alarm): List<Alarm>
}
