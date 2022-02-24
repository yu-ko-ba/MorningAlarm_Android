package com.example.morningalarm.android.data.infra.provider

import com.example.morningalarm.android.data.infra.HostSelectInterceptor
import okhttp3.OkHttpClient

class OkHttpClientProvider(hostSelectInterceptor: HostSelectInterceptor) {
    val client: OkHttpClient

    init {
        val builder = OkHttpClient().newBuilder().addInterceptor(hostSelectInterceptor)
        client = builder.build()
    }
}
