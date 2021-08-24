package com.rafabap.flickergallery.domain.usecase

import com.rafabap.flickergallery.data.mapper.PhotoSizeMapper
import com.rafabap.flickergallery.domain.model.Photo
import com.rafabap.flickergallery.domain.repository.IPhotoRepository
import io.reactivex.rxjava3.core.Observer

class PhotoUseCase(private val repository: IPhotoRepository,
                   private val photoSizeMapper: PhotoSizeMapper) : BaseUseCase() {
    fun callSearchApi(page: Int, observer: Observer<Photo>) {
        if (page == VAL_FIRST_PAGE) {
            repository.fetchSearchPhoto()
                .map {
                    photoSizeMapper.mapWithSize(it)
                }
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(observer)
        } else {
            repository.fetchSearchPhotoByPage(page)
                .map {
                    photoSizeMapper.mapWithSize(it)
                }
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(observer)
        }
    }

    private companion object {
        const val VAL_FIRST_PAGE = 1
    }
}