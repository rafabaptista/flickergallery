package com.rafabap.flickergallery.presentation.view.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafabap.flickergallery.R
import com.rafabap.flickergallery.databinding.ActivityPhotoListBinding
import com.rafabap.flickergallery.presentation.view.Navigation
import com.rafabap.flickergallery.presentation.view.NetworkState
import com.rafabap.flickergallery.presentation.view.adapter.PhotoListAdapter
import com.rafabap.flickergallery.presentation.view.viewmodel.PhotoListViewModel
import com.rafabap.flickergallery.presentation.view.viewmodel.PhotoListViewModel.Companion.ERROR_LOADING_SEARCH_PHOTOS
import org.koin.android.ext.android.inject

class PhotoListActivity : BaseActivity(),
    PhotoListAdapter.PhotoListViewHolder.PhotoListAdapterEvents {

    private lateinit var binding: ActivityPhotoListBinding
    private lateinit var photoListAdapter: PhotoListAdapter

    val viewModel: PhotoListViewModel by inject()

    var loadingMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoListBinding.inflate(layoutInflater)
        val view = binding.root
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        setContentView(view)
        viewModel.loaPhotos()
        setupView()
        initiateListeners()
        initiateObservers()
    }

    override fun onPhotoClick(imageViewPhoto: ImageView, photoUrl: String) {
        Navigation.openPhotoComponent(
            this@PhotoListActivity,
            imageViewPhoto,
            photoUrl
        )
    }

    private fun initiateObservers() {
        observe(viewModel.networkState) {
            when (it?.status) {
                NetworkState.NetworkStateStatus.RUNNING -> {
                    startRefreshing()
                }
                NetworkState.NetworkStateStatus.SUCCESS -> {
                    stopRefreshing()
                }
                NetworkState.NetworkStateStatus.ERROR -> {
                    showMessage(it.detail)
                    stopRefreshing()
                }
                NetworkState.NetworkStateStatus.EMPTY -> {
                    showToast(getString(R.string.message_empty_images))
                    stopRefreshing()
                }
                else -> {
                    stopRefreshing()
                }
            }
        }
        observe(viewModel.photoList) {
            it?.let { photoList ->
                photoListAdapter.submitList(photoList)
                photoListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showMessage(error: String?) {
        val message = when (error) {
            ERROR_LOADING_SEARCH_PHOTOS -> {
                getString(R.string.message_error_loading_images)
            }
            else -> {
                null
            }
        }
        showToast(message)
    }

    private fun startRefreshing() {
        binding.run {
            photoListSwipe.isEnabled = false
            photoListSwipe.isRefreshing = true
            loadingMore = true
        }
    }

    private fun stopRefreshing() {
        binding.run {
            photoListSwipe.isEnabled = true
            photoListSwipe.isRefreshing = false
            loadingMore = false
        }
    }

    private fun setupView() {
        photoListAdapter = PhotoListAdapter(this)
        binding.run {
            recyclerFeed.layoutManager = GridLayoutManager(
                this@PhotoListActivity,
                COLUMN_NUMBER
            )
            recyclerFeed.adapter = ConcatAdapter(photoListAdapter)
        }
    }

    private fun initiateListeners() {
        binding.run {
            photoListSwipe.isEnabled = false
            photoListSwipe.setOnRefreshListener {
                viewModel.loaPhotos()
            }
            if (viewModel.hasInternet()) {
                recyclerFeed.addOnScrollListener(setInfiniteScrolling())
            }
        }
    }

    private fun setInfiniteScrolling() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager: LinearLayoutManager? =
                recyclerView.layoutManager as LinearLayoutManager?
            if (layoutManager == null) {
                return
            } else {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    if (!loadingMore) {
                        loadingMore = true
                        viewModel.loaPhotos()
                    }
                }
            }
        }
    }

    companion object {
        const val COLUMN_NUMBER = 2
    }
}