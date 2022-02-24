package com.example.morningalarm.android.ui.uistate

data class AlarmListItemUiState(
    val id: String,
    val timeText: String,
    var isSynchronized: Boolean = true
)
