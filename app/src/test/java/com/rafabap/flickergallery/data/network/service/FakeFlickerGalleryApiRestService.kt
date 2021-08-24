package com.rafabap.flickergallery.data.network.service

import com.rafabap.flickergallery.data.model.SearchServiceResponse
import com.rafabap.flickergallery.data.model.SizesServiceResponse
import io.reactivex.rxjava3.core.Observable

class FakeFlickerGalleryApiRestService : IFlickerGalleryApiRestService {

    private val resultSearch = SearchServiceResponse()
    private val resultSize = SizesServiceResponse()

    override fun searchPhoto(pageNumber: String): Observable<SearchServiceResponse> =
        Observable.just(resultSearch)

    override fun photoSize(photoId: String): Observable<SizesServiceResponse> =
        Observable.just(resultSize)
}