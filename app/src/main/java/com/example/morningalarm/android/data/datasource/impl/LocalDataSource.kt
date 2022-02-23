package com.example.morningalarm.android.data.datasource.impl

import com.example.morningalarm.android.data.datasource.AlarmDataSource
import com.example.morningalarm.android.domain.model.Alarm

// ローカル実行テスト用のデータソース
class LocalDataSource : AlarmDataSource {
    private val alarmList: ArrayList<Alarm> = arrayListOf()

    override suspend fun fetch(): List<Alarm> = alarmList

    override suspend fun add(alarm: Alarm): List<Alarm> {
        alarmList.add(alarm)
        return alarmList
    }
}
