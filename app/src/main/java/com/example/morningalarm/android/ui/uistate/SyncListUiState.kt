package com.example.morningalarm.android.ui.uistate

sealed class SyncListUiState (
    val isRefreshing: Boolean = false
){
    object NotLoaded : SyncListUiState(isRefreshing = false)

    object Loading : SyncListUiState(isRefreshing = true)

    object Success : SyncListUiState(isRefreshing = false)

    object Failure : SyncListUiState(isRefreshing = false)
}
