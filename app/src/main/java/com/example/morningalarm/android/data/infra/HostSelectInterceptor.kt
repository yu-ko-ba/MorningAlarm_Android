package com.example.morningalarm.android.data.infra

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.net.URISyntaxException

// Based on https://medium.com/nerd-for-tech/change-retrofit-base-url-on-runtime-2036ef1dee44
class HostSelectInterceptor constructor(private val preferenceHelper: PreferenceHelper) : Interceptor {
    private lateinit var host: HttpUrl

    init {
        setHostBaseUrl()
    }

    fun setHostBaseUrl() {
        val hostUrl = preferenceHelper.getHostUrl()
        host = HttpUrl.Builder().scheme("http") .host(hostUrl).build()
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        var newUrl: HttpUrl? = null
        try {
            newUrl = request.url.newBuilder()
                .scheme(host.scheme)
                .host(host.toUrl().toURI().host)
                .build()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        assert(newUrl != null)
        request = request.newBuilder()
            .url(newUrl!!)
            .build()
        return chain.proceed(request)
    }
}
