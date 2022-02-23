package com.example.morningalarm.android.data.infra.provider

import okhttp3.OkHttpClient

class OkHttpClientProvider {
    val client: OkHttpClient

    init {
        val builder = OkHttpClient().newBuilder()
        client = builder.build()
    }
}
