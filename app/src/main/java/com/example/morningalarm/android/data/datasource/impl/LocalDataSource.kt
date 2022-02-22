package com.example.morningalarm.android.data.datasource.impl

import com.example.morningalarm.android.data.datasource.AlarmDataSource
import com.example.morningalarm.android.domain.model.Alarm

// ローカル実行テスト用のデータソース
class LocalDataSource : AlarmDataSource {
    private val alarmList: List<Alarm> = mutableListOf()

    override suspend fun fetch(): List<Alarm> = alarmList
}
