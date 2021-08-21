package com.example.morningalarm.android

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object MorningAlarmManager {

    var serverAddress = "192.168.128.207"
    var portNumber = "5000"

    private var data = JSONObject("{}")

    private var onFailedListener: () -> Unit = {}


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


    fun setOnFailedListener(action: () -> Unit) {
        onFailedListener = action
    }


    private fun getBaseUrl(): String {
        return "http://${serverAddress}:${portNumber}"
    }


    private fun getJsonString(url: URL): String {
        return runBlocking(Dispatchers.IO) {
            var json = ""
            for (count in 0..100) {
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
                    break
                } catch (e: Exception) {
                    println("JSONの取得に失敗しました")
                }
            }
            json
        }
    }


    private fun parseJSON(json: String, onSucceedListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
            println("JSONのパースに成功しました")
            onSucceedListener()
        } catch (e: Exception) {
            println("JSONのパースに失敗しました")
            onFailedAdditionalListener()
            onFailedListener()
        }

        return jsonObject
    }


    fun get(onSucceedListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("get")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/list")), onSucceedListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }


    fun add(hour: Int, minute: Int, onSucceedListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("add")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/add/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")), onSucceedListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }


    fun delete(id: String, onSucceedListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("ID: $id")
        println("deleted")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/delete/${id}")), onSucceedListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }


    fun change(id: String, hour: Int, minute: Int, onSucceedListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("change")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/change/${id}/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")), onSucceedListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }
}