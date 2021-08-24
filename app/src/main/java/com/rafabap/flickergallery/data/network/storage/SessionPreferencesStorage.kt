package com.rafabap.flickergallery.data.network.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.rafabap.flickergallery.BuildConfig
import com.rafabap.flickergallery.data.model.PhotoResponse
import com.rafabap.flickergallery.data.network.storage.SessionPreferences.Companion.CACHE_PHOTO_LIST
import com.rafabap.flickergallery.presentation.util.extension.fromJson
import io.reactivex.rxjava3.core.Observable

interface SessionPreferences {

    companion object {
        const val CACHE_PHOTO_LIST = "CACHE_PHOTO_LIST"
    }

    fun savePhotoData(data: PhotoResponse)
    fun loadPhotoData(): Observable<PhotoResponse>
    fun hasCachedData(): Boolean
    fun clearData()
}

// TODO - DISCLAIMER - Implemented only for test purposes. A DB would be the best option for Production

@SuppressLint("ApplySharedPref")
class SessionPreferencesStorage(context: Context) : SessionPreferences {

    private val sessionStorage: SharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "session", Context.MODE_PRIVATE)

    override fun savePhotoData(data: PhotoResponse) {
        val map = Gson().fromJson<MutableMap<String, PhotoResponse>>(sessionStorage.getString(CACHE_PHOTO_LIST, ""))
            ?: mutableMapOf()
        data.id?.let {
            map[it] = data
        }
        sessionStorage.edit().putString(CACHE_PHOTO_LIST, Gson().toJson(map)).commit()
    }

    override fun loadPhotoData(): Observable<PhotoResponse> {
        val map = Gson().fromJson<MutableMap<String, PhotoResponse>>(sessionStorage.getString(CACHE_PHOTO_LIST, ""))
            ?: mutableMapOf()
        val data = map.map { it.value }
        return Observable.fromIterable(data)
    }

    override fun hasCachedData(): Boolean {
        val map = Gson().fromJson<MutableMap<String, PhotoResponse>>(sessionStorage.getString(CACHE_PHOTO_LIST, ""))
            ?: mutableMapOf()
        return map.isNotEmpty()
    }

    override fun clearData() {
        sessionStorage.edit().clear().commit()
    }
}