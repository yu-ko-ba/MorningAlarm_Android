package com.example.morningalarm.android.data.datasource.impl

import com.example.morningalarm.android.data.datasource.AlarmDataSource
import com.example.morningalarm.android.domain.model.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ローカル実行テスト用のデータソース
class LocalDataSource : AlarmDataSource {
    private val alarmList: ArrayList<Alarm> = arrayListOf()

    override suspend fun fetch(): List<Alarm> = withContext(Dispatchers.IO) {
        // 通信遅延を再現するには Thread.sleep(3000) など入れてもらえれば
        alarmList
    }

    override suspend fun add(alarm: Alarm): List<Alarm> = withContext(Dispatchers.IO) {
        // Thread.sleep(3000)
        alarmList.add(alarm)
        alarmList
    }
}
