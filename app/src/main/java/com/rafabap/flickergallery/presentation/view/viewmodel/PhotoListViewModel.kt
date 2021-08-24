package com.rafabap.flickergallery.presentation.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rafabap.flickergallery.domain.model.Photo
import com.rafabap.flickergallery.domain.usecase.PhotoUseCase
import com.rafabap.flickergallery.presentation.util.connection.InternetConnectionVerifier
import com.rafabap.flickergallery.presentation.view.NetworkState
import empty
import error
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import running
import success

class PhotoListViewModel(private val useCase: PhotoUseCase,
                         private val internetVerification: InternetConnectionVerifier) : ViewModel() {

    var networkState = MutableLiveData<NetworkState>()
    val photoList = MutableLiveData<ArrayList<Photo>>()
    var photoListTemp = ArrayList<Photo>()
    var page = FIRST_PAGE

    fun loaPhotos() {
        useCase.callSearchApi(page, object : Observer<Photo> {
            override fun onSubscribe(d: Disposable?) {
                if (page == FIRST_PAGE) {
                    photoListTemp = ArrayList()
                }
                networkState.running()
            }

            override fun onNext(response: Photo?) {
                response?.let { photo ->
                    updateData(photo)
                }
            }

            override fun onError(e: Throwable?) {
                Log.e("TAG_ERROR", e?.message ?: "")
                networkState.error(ERROR_LOADING_SEARCH_PHOTOS)
            }

            override fun onComplete() {
                if (photoListTemp.isEmpty()) {
                    networkState.empty()
                } else {
                    page++
                    networkState.success()
                }
            }

        })
    }

    fun hasInternet() = internetVerification.isConnectedToInternet()

    private fun updateData(photo: Photo) {
        photoListTemp.add(photo)
        photoList.postValue(photoListTemp)
        photoList.value = photoListTemp
    }

    companion object {
        const val ERROR_LOADING_SEARCH_PHOTOS = "ERROR_LOADING_SEARCH_PHOTOS"
        const val FIRST_PAGE = 1
    }
}