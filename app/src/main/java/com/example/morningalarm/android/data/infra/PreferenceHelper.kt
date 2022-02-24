package com.example.morningalarm.android.data.infra

import android.content.Context
import com.example.morningalarm.android.R

class PreferenceHelper(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preferences_name),
        Context.MODE_PRIVATE
    )

    fun getHostUrl() = "${serverAddress()}:${portNumber()}"

    private fun serverAddress() = sharedPreferences.getString(
        context.getString(R.string.server_address_key),
        context.getString(R.string.default_server_address)
    )

    private fun portNumber() = sharedPreferences.getString(
        context.getString(R.string.port_number_key),
        context.getString(R.string.default_port_number)
    )
}
