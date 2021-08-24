package com.rafabap.flickergallery.data.network.api

import com.rafabap.flickergallery.data.model.PhotoResponse
import com.rafabap.flickergallery.data.model.SizeResponse
import com.rafabap.flickergallery.data.network.service.IFlickerGalleryApiRestService
import io.reactivex.rxjava3.core.Observable
import java.util.*


class FlickerGalleryApi : BaseApi() {

    private val apiRestClient: IFlickerGalleryApiRestService

    init {
        val retrofit = build()
        apiRestClient = retrofit.create(IFlickerGalleryApiRestService::class.java)
    }

    fun searchPhotoWithSize(page: Int): Observable<PhotoResponse> {
        return apiRestClient.searchPhoto(page.toString())
            .flatMapIterable {
                it.result?.photoList ?: ArrayList()
            }
            .publish { obsPhotoResponse ->
                Observable.zip(
                    obsPhotoResponse,
                    obsPhotoResponse.flatMap { photoResponse ->
                        photoSize(photoResponse.id ?: "0")
                    },
                    { photo, sizes ->
                        photo.apply {
                            this.sizes = sizes
                        }
                    }
                )
            }
            .onErrorResumeNext {
                Observable.empty<PhotoResponse>()
            }
    }

    private fun photoSize(idPhoto: String): Observable<List<SizeResponse>>  {
        val defaultLocale = Locale.getDefault()
        return apiRestClient.photoSize(idPhoto)
            .map { sizeResponse ->
            sizeResponse?.result?.sizeList?.filter {
                it.label?.lowercase(defaultLocale) == SERVICE_VALUE_PHOTO_LABEL_NORMAL
                        || it.label?.lowercase(defaultLocale) == SERVICE_VALUE_PHOTO_LABEL_LARGE
            } ?: emptyList()
        }.onErrorResumeNext {
            Observable.empty<List<SizeResponse>>()
        }
    }

    companion object {
        const val SERVICE_VALUE_PHOTO_LABEL_NORMAL = "large square"
        const val SERVICE_VALUE_PHOTO_LABEL_LARGE = "large"
    }
}
