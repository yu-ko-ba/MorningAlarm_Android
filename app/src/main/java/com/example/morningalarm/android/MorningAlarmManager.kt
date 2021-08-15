package com.example.morningalarm.android

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object MorningAlarmManager {
    var serverAddress = "192.168.128.207"
    var portNumber = "5000"

    var firstFragment: FirstFragment? = null


    private fun getBaseUrl(): String {
        return "http://${serverAddress}:${portNumber}"
    }

    private fun getJsonString(url: URL): String {
        val json = runBlocking(Dispatchers.IO) {
            var json = ""
            for (count in 0..2) {
                try {
                    val br = BufferedReader(InputStreamReader(url.openStream()))
                    json = runBlocking(Dispatchers.Default) {
                        var json = ""
                        for (line in br.lines()) {
                             json += line
                        }
                        json
                    }
                    println("JSONの取得に成功しました")
                    break
                } catch (e: Exception) {
                    println("JSONの取得に失敗しました")
                }
            }
            json
        }
        return json
    }


    private fun parseJSON(json: String): JSONObject {
        var jsonObject = JSONObject("{}")
        try {
            jsonObject = JSONObject(json)
            println("JSONのパースに成功しました")
        } catch (e: Exception) {
            println("JSONのパースに失敗しました")
        }
        return jsonObject
    }


    fun get(): JSONObject {
        return parseJSON(getJsonString(URL("${getBaseUrl()}/list")))
    }


    fun add(hour: Int, minute: Int): JSONObject {
        return parseJSON(getJsonString(URL("${getBaseUrl()}/add/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")))
    }


    fun delete(id: String): JSONObject {
        return parseJSON(getJsonString(URL("${getBaseUrl()}/delete/${id}")))
    }


    fun change(id: String, hour: Int, minute: Int): JSONObject {
        return parseJSON(getJsonString(URL("${getBaseUrl()}/change/${id}/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")))
    }
}