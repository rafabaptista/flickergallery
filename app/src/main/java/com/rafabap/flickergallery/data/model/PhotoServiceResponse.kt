package com.rafabap.flickergallery.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class PhotoServiceResponse(
    val page: Int?,
    @SerializedName("pages") val totalPages: Int? = null,
    @SerializedName("perpage") val photosPerPages: Int? = null,
    @SerializedName("total") val totalPhotos: Int? = null,
    @SerializedName("photo") val photoList: List<PhotoResponse>? = null
) : Serializable