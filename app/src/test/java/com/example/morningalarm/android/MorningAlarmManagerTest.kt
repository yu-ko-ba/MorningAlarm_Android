package com.example.morningalarm.android

import junit.framework.TestCase

class MorningAlarmManagerTest : TestCase() {

    public override fun setUp() {
        super.setUp()
    }

    public override fun tearDown() {}

    fun testGetAlarmList() {
        MorningAlarmManager.get()
    }
}