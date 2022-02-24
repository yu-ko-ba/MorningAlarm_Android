package com.example.morningalarm.android.data.infra.provider

import com.squareup.moshi.Moshi

object MoshiProvider {

    val moshi: Moshi = Moshi.Builder()
        .build()
}
