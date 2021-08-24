package com.rafabap.flickergallery.presentation.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.rafabap.flickergallery.R
import com.rafabap.flickergallery.presentation.view.Navigation.Params.PARAM_VALUE_IMAGE_URL
import com.rafabap.flickergallery.presentation.view.activity.PhotoExhibitionComponentActivity

class Navigation {
    companion object {
        @JvmStatic
        fun openPhotoComponent(
            context: Context,
            imagePlace: View,
            url: String
        ) {
            val intent = Intent(context, PhotoExhibitionComponentActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                imagePlace,
                context.getString(R.string.param_transition_fundraiser_image)
            )
            intent.putExtra(PARAM_VALUE_IMAGE_URL, url)
            context.startActivity(intent, options.toBundle())
        }
    }

    object Params {
        const val PARAM_VALUE_IMAGE_URL = "image_url"
    }
}