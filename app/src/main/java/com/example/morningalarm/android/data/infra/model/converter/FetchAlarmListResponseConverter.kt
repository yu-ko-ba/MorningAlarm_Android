package com.example.morningalarm.android.data.infra.model.converter

import com.example.morningalarm.android.data.infra.model.FetchAlarmListResponse
import com.example.morningalarm.android.domain.model.Alarm

class FetchAlarmListResponseConverter {

    fun toAlarmList(fetchAlarmListResponse : FetchAlarmListResponse) = fetchAlarmListResponse.list.map {
        Alarm(
            it.id,
            it.hour,
            it.minute
        )
    }
}
