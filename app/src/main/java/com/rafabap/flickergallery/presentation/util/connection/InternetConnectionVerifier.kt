package com.rafabap.flickergallery.presentation.util.connection

interface InternetConnectionVerifier {

    fun isConnectedToInternet(): Boolean

    fun getInternetConnectionType(): InternetConnectionType

    enum class InternetConnectionType {
        MOBILE_DATA,
        WIFI,
        ETHERNET,
        NO_CONNECTION
    }
}