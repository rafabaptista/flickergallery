package com.rafabap.flickergallery.presentation.util.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.rafabap.flickergallery.presentation.util.extension.checkInternetConnection

class DefaultInternetConnectionVerifier(val context: Context) : InternetConnectionVerifier {

    override fun isConnectedToInternet() = context.checkInternetConnection()

    override fun getInternetConnectionType(): InternetConnectionVerifier.InternetConnectionType {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> InternetConnectionVerifier.InternetConnectionType.WIFI
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> InternetConnectionVerifier.InternetConnectionType.MOBILE_DATA
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> InternetConnectionVerifier.InternetConnectionType.ETHERNET
                    else -> InternetConnectionVerifier.InternetConnectionType.NO_CONNECTION
                }
            } ?: InternetConnectionVerifier.InternetConnectionType.NO_CONNECTION
        }
    }
}