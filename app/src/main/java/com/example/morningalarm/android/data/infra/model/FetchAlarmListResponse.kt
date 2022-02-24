package com.example.morningalarm.android.data.infra.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchAlarmListResponse(
    @Json(name = "data") val list: List<Contents>
) {

    @JsonClass(generateAdapter = true)
    data class Contents(
        @Json(name = "id") val id: String,
        @Json(name = "hour") val hour: Int,
        @Json(name = "minute") val minute: Int,
    )
}
