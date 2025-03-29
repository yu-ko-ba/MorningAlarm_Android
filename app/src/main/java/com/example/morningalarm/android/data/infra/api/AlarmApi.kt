package com.example.morningalarm.android.data.infra.api

import com.example.morningalarm.android.data.infra.model.FetchAlarmListResponse
import retrofit2.http.GET

interface AlarmApi {

    @GET("list/")
    suspend fun fetchAlarmList(): FetchAlarmListResponse
}
