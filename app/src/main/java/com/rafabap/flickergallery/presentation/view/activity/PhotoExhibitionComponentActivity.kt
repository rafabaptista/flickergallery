package com.rafabap.flickergallery.presentation.view.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.rafabap.flickergallery.R
import com.rafabap.flickergallery.databinding.ActivityPhotoExhibitionComponentBinding
import com.rafabap.flickergallery.presentation.view.Navigation.Params.PARAM_VALUE_IMAGE_URL
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PhotoExhibitionComponentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoExhibitionComponentBinding

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoExhibitionComponentBinding.inflate(layoutInflater)
        val view = binding.root
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        setContentView(view)
        checkExtra()
        setupView()
        initiateListeners()
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    private fun initiateListeners() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun checkExtra() {
        this.url = intent.getStringExtra(PARAM_VALUE_IMAGE_URL) ?: ""
    }

    private fun setupView() {
        setupPhoto()
    }

    private fun setupPhoto() {
        binding.run {
            photoPlace.transitionName = getString(R.string.param_transition_fundraiser_image)
            Picasso.get()
                .load(url)
                .error(R.mipmap.empty_photo)
                .into(photoPlace, object: Callback {
                    override fun onSuccess() {
                        ActivityCompat.startPostponedEnterTransition(this@PhotoExhibitionComponentActivity)
                    }

                    override fun onError(e: Exception?)  {
                        ActivityCompat.startPostponedEnterTransition(this@PhotoExhibitionComponentActivity)
                    }
                })
        }
    }
}