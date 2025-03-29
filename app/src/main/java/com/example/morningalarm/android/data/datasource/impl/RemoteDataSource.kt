package com.example.morningalarm.android.data.datasource.impl

import android.content.Context
import com.example.morningalarm.android.data.datasource.AlarmDataSource
import com.example.morningalarm.android.data.infra.HostSelectInterceptor
import com.example.morningalarm.android.data.infra.provider.ApiProvider
import com.example.morningalarm.android.data.infra.api.AlarmApi
import com.example.morningalarm.android.data.infra.model.converter.FetchAlarmListResponseConverter
import com.example.morningalarm.android.domain.model.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource(
    private val context: Context,
    private val hostSelectInterceptor: HostSelectInterceptor = HostSelectInterceptor(context),
    private val alarmApi: AlarmApi = ApiProvider(hostSelectInterceptor).alarmApi,
    private val fetchAlarmListResponseConverter: FetchAlarmListResponseConverter = FetchAlarmListResponseConverter()
): AlarmDataSource {

    override suspend fun fetch(): List<Alarm> = withContext(Dispatchers.IO) {
        fetchAlarmListResponseConverter.toAlarmList(alarmApi.fetchAlarmList())
    }

    override suspend fun add(alarm: Alarm): List<Alarm> {
        TODO()
    }
}
