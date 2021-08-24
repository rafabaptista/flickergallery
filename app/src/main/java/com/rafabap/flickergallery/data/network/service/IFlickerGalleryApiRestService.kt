package com.rafabap.flickergallery.data.network.service

import com.rafabap.flickergallery.data.model.SearchServiceResponse
import com.rafabap.flickergallery.data.model.SizesServiceResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IFlickerGalleryApiRestService {

    @GET("?method=flickr.photos.search&tags=dogs&format=json&nojsoncallback=1")
    fun searchPhoto(@Query("page") pageNumber: String): Observable<SearchServiceResponse>

    @GET("?method=flickr.photos.getSizes&format=json&nojsoncallback=1")
    fun photoSize(@Query("photo_id") photoId: String): Observable<SizesServiceResponse>

}