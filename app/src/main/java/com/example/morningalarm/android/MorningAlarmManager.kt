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

    private var onOperationStartListener: () -> Unit = {}
    private var onSucceedListener: () -> Unit = {}
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


    fun setOnOperationStartListener(listener: () -> Unit) {
        onOperationStartListener = listener
    }


    fun setOnSucceedListener(listener: () -> Unit) {
        onSucceedListener = listener
    }


    fun setOnFailedListener(listener: () -> Unit) {
        onFailedListener = listener
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


    private fun parseJSON(json: String, onSucceedAdditionalListener: () -> Unit, onFailedAdditionalListener: () -> Unit): JSONObject? {
        onOperationStartListener()

        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
            println("JSONのパースに成功しました")
            onSucceedAdditionalListener()
            onSucceedListener()
        } catch (e: Exception) {
            println("JSONのパースに失敗しました")
            onFailedListener()
            onFailedAdditionalListener()
        }

        return jsonObject
    }


    fun get(onSucceedAdditionalListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("get")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/list")), onSucceedAdditionalListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }


    fun get(onSucceedAdditionalListener: () -> Unit = {}) {
        get(onSucceedAdditionalListener, {})
    }


    fun add(hour: Int, minute: Int, onSucceedAdditionalListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("add")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/add/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")), onSucceedAdditionalListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }


    fun add(hour: Int, minute: Int, onSucceedAdditionalListener: () -> Unit = {}) {
        add(hour, minute, onSucceedAdditionalListener, {})
    }


    fun delete(id: String, onSucceedAdditionalListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("ID: $id")
        println("deleted")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/delete/${id}")), onSucceedAdditionalListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }


    fun delete(id: String, onSucceedAdditionalListener: () -> Unit = {}) {
        delete(id, onSucceedAdditionalListener, {})
    }


    fun change(id: String, hour: Int, minute: Int, onSucceedAdditionalListener: () -> Unit = {}, onFailedAdditionalListener: () -> Unit = {}) {
        println("change")
        CoroutineScope(Dispatchers.Default).launch {
            parseJSON(getJsonString(URL("${getBaseUrl()}/change/${id}/${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")), onSucceedAdditionalListener, onFailedAdditionalListener)?.getJSONObject("data")?.let {
                data = it
            }
        }
    }


    fun change(id: String, hour: Int, minute: Int, onSucceedAdditionalListener: () -> Unit = {}) {
        change(id, hour, minute, onSucceedAdditionalListener, {})
    }
}