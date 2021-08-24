package com.rafabap.flickergallery.domain.repository

import com.rafabap.flickergallery.data.model.PhotoResponse
import com.rafabap.flickergallery.data.network.api.FlickerGalleryApi
import com.rafabap.flickergallery.data.network.storage.SessionPreferences
import com.rafabap.flickergallery.presentation.util.connection.InternetConnectionVerifier
import io.reactivex.rxjava3.core.Observable

class PhotoRepository(private val apiRestClient: FlickerGalleryApi,
                      private val storage: SessionPreferences,
                      private val internetVerification: InternetConnectionVerifier) : IPhotoRepository {

    override fun fetchSearchPhoto(): Observable<PhotoResponse> {
        return if (internetVerification.isConnectedToInternet()) {
            val cachedPhotos = storage.loadPhotoData()
            val photos = apiRestClient.searchPhotoWithSize(1)
            Observable.merge(photos, cachedPhotos)
                .distinct {
                    it.id
                }
                .doOnNext {
                    storage.savePhotoData(it)
                }
        } else {
            storage.loadPhotoData()
        }
    }

    override fun fetchSearchPhotoByPage(page: Int): Observable<PhotoResponse> {
        return apiRestClient.searchPhotoWithSize(page)
    }
}