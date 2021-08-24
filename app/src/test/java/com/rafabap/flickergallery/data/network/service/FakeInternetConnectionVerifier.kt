package com.rafabap.flickergallery.data.network.service

import com.rafabap.flickergallery.presentation.util.connection.InternetConnectionVerifier

class FakeInternetConnectionVerifier: InternetConnectionVerifier {
    override fun isConnectedToInternet(): Boolean {
        return true
    }

    override fun getInternetConnectionType(): InternetConnectionVerifier.InternetConnectionType {
        return InternetConnectionVerifier.InternetConnectionType.WIFI
    }

}