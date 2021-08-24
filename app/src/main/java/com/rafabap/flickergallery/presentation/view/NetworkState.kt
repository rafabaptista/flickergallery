package com.rafabap.flickergallery.presentation.view

data class NetworkState(val status: NetworkStateStatus, val detail: String?) {
    enum class NetworkStateStatus {
        RUNNING, SUCCESS, EMPTY, ERROR
    }
}