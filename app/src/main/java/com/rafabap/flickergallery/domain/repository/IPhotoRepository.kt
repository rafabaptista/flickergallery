package com.rafabap.flickergallery.domain.repository

import com.rafabap.flickergallery.data.model.PhotoResponse
import io.reactivex.rxjava3.core.Observable

interface IPhotoRepository {
    fun fetchSearchPhoto(): Observable<PhotoResponse>
    fun fetchSearchPhotoByPage(page: Int): Observable<PhotoResponse>
}