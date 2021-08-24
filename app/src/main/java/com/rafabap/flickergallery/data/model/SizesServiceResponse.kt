package com.rafabap.flickergallery.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SizesServiceResponse(
    @SerializedName("sizes") val result: SizeServiceResponse? = null
) : Serializable