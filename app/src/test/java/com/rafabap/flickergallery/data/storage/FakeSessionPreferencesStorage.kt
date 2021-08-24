package com.rafabap.flickergallery.data.storage

import com.rafabap.flickergallery.data.model.PhotoResponse
import com.rafabap.flickergallery.data.network.storage.SessionPreferences
import io.reactivex.rxjava3.core.Observable

class FakeSessionPreferencesStorage : SessionPreferences {

    private val map = mutableMapOf<String?, PhotoResponse?>()

    override fun savePhotoData(data: PhotoResponse) {
        map[data.id] = data
    }

    override fun loadPhotoData(): Observable<PhotoResponse> {
        val data = map.map { it.value }
        return if (data.isEmpty()) {
            Observable.fromIterable(data)
        } else {
            Observable.error(Exception())
        }
    }

    override fun hasCachedData(): Boolean {
        return map.isNotEmpty()
    }

    override fun clearData() {
        map.clear()
    }
}