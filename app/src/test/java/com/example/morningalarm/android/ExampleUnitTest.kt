package com.example.morningalarm.android

import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun getAlarmTest() {
        val url = URL("http://api.openweathermap.org/data/2.5/weather?lat=34&lon=140&appid=6d343628549b6f099e5031718b63d6b1")
        val br = BufferedReader(InputStreamReader(url.openStream()))
        var json = ""
        for (line in br.lines()) {
            json += line
        }
        println("----------------------")
        println(JSONObject(json).toString(4))
        println("----------------------")
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        println("Hello, World!")
    }
}