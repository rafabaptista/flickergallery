package com.rafabap.flickergallery.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class PhotoResponse(
    val id: String? = null,
    var sizes: List<SizeResponse>? = null
) : Serializable