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

    private var onSucceeded: () -> Unit = {}
    private var onFailed: () -> Unit = {}

    private var data = JSONObject("{\"data\":{}}")


    fun getData(): JSONObject {
        return data
    }


    fun getKeys(): List<String> {
        val keys = mutableListOf<String>()
        for (key in data.keys()) {
            keys.add(key)
        }

        return keys.toList()
    }


    fun setOnSucceeded(action: () -> Unit) {
        onSucceeded = action
    }


    fun setOnFailed(action: () -> Unit) {
        onFailed = action
    }


    private fun getBaseUrl(): String {
        return "http://${serverAddress}:${portNumber}"
    }


    private fun getJsonString(url: URL): String {
        var succeeded = false
        val json = runBlocking(Dispatchers.IO) {
            var json = ""
            for (count in 0..2) {
                try {
                    val br = BufferedReader(InputStreamReader(url.openStream()))
                    json = runBlocking(Dispatchers.Default) {
                        var s = ""
                        for (line in br.lines()) {
                            s += line
                        }
                        s
                    }
                    println("JSONの取得に成功しました")
                    succeeded = true
                    break
                } catch (e: Exception) {
                    println("JSONの取得に失敗しました")
                }
            }
            json
        }

        if (succeeded) {
            onSucceeded()
        } else {
            onFailed()
        }
        onSucceeded()

        return json
    }


    private fun parseJSON(json: String): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
            println("JSONのパースに成功しました")
        } catch (e: Exception) {
            println("JSONのパースに失敗しました")
        }

        return jsonObject
    }


    fun get(): JSONObject {
        println("get")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/list")))?.getJSONObject("data") ?: data
    }


    fun add(hour: Int, minute: Int): JSONObject {
        println("add")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/add/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")))?.getJSONObject("data") ?: data
    }


    fun delete(id: String): JSONObject {
        println("ID: $id")
        println("deleted")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/delete/${id}")))?.getJSONObject("data") ?: data
    }


    fun change(id: String, hour: Int, minute: Int): JSONObject {
        println("change")
        return parseJSON(getJsonString(URL("${getBaseUrl()}/change/${id}/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")))?.getJSONObject("data") ?: data
    }
}