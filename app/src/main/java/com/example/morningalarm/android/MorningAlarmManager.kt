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

    var data = get().getJSONObject("data")

    var firstFragment: FirstFragment? = null


    fun getKeys(): List<String> {
        val keys = mutableListOf<String>()
        for (key in data.keys()) {
            keys.add(key)
        }

        return keys.toList()
    }


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
        var jsonObject = JSONObject("{\"data\":{}}")
        try {
            jsonObject = JSONObject(json)
            println("JSONのパースに成功しました")
        } catch (e: Exception) {
            println("JSONのパースに失敗しました")
        }

        data = jsonObject.getJSONObject("data")

        return jsonObject
    }


    fun get(): JSONObject {
        println("get")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/list")))
    }


    fun add(hour: Int, minute: Int): JSONObject {
        println("add")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/add/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")))
    }


    fun delete(id: String): JSONObject {
        println("delete")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/delete/${id}")))
    }


    fun change(id: String, hour: Int, minute: Int): JSONObject {
        println("change")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/change/${id}/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")))
    }
}