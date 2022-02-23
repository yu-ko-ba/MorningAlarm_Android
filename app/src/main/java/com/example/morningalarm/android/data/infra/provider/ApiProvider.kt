package com.example.morningalarm.android.data.infra.provider

import com.example.morningalarm.android.data.infra.HostSelectInterceptor
import com.example.morningalarm.android.data.infra.api.AlarmApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiProvider(
    private val hostSelectInterceptor: HostSelectInterceptor,
    client: OkHttpClient = OkHttpClientProvider(hostSelectInterceptor).client,
    moshi: Moshi = MoshiProvider.moshi
) {
    private var serverAddress = "192.168.128.207"
    private var portNumber = "5000"

    private val baseRetrofit = Retrofit.Builder()
        .baseUrl("$serverAddress:$portNumber")
        .build()

    private fun buildMoshiRetrofit(baseRetrofit: Retrofit, client: OkHttpClient, moshi: Moshi) =
        baseRetrofit.newBuilder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    val alarmApi = buildMoshiRetrofit(
        baseRetrofit,
        client,
        moshi
    ).create(AlarmApi::class.java)
}
