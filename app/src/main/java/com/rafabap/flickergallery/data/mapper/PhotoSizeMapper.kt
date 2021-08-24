package com.rafabap.flickergallery.data.mapper

import com.rafabap.flickergallery.data.model.PhotoResponse
import com.rafabap.flickergallery.data.network.api.FlickerGalleryApi.Companion.SERVICE_VALUE_PHOTO_LABEL_LARGE
import com.rafabap.flickergallery.data.network.api.FlickerGalleryApi.Companion.SERVICE_VALUE_PHOTO_LABEL_NORMAL
import com.rafabap.flickergallery.domain.model.Photo
import java.util.*

class PhotoSizeMapper {
    fun mapWithSize(photoResponse: PhotoResponse): Photo {
        val defaultLocale = Locale.getDefault()
        var normalPhotoUrl = ""
        var largePhotoUrl = ""
        photoResponse.sizes?.forEach { sizePhoto ->
            if (sizePhoto.label?.lowercase(defaultLocale) == SERVICE_VALUE_PHOTO_LABEL_NORMAL) {
                normalPhotoUrl = sizePhoto.source ?: ""
            } else  if (sizePhoto.label?.lowercase(defaultLocale) == SERVICE_VALUE_PHOTO_LABEL_LARGE) {
                largePhotoUrl = sizePhoto.source ?: ""
            }
        }
        return Photo(
            id = photoResponse.id ?: "",
            normalPhotoUrl = normalPhotoUrl,
            largePhotoUrl = largePhotoUrl
        )
    }
}