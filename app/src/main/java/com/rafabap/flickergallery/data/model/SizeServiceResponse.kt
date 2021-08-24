package com.rafabap.flickergallery.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SizeServiceResponse(
    @SerializedName("size") val sizeList: List<SizeResponse>
) : Serializable