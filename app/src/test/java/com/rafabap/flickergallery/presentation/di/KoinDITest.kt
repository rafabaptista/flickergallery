package com.rafabap.flickergallery.presentation.di

import com.rafabap.flickergallery.data.mapper.PhotoSizeMapper
import com.rafabap.flickergallery.data.network.api.FlickerGalleryApi
import com.rafabap.flickergallery.data.network.service.FakeFlickerGalleryApiRestService
import com.rafabap.flickergallery.data.network.service.FakeInternetConnectionVerifier
import com.rafabap.flickergallery.data.network.service.IFlickerGalleryApiRestService
import com.rafabap.flickergallery.data.network.storage.SessionPreferences
import com.rafabap.flickergallery.data.storage.FakeSessionPreferencesStorage
import com.rafabap.flickergallery.domain.repository.IPhotoRepository
import com.rafabap.flickergallery.domain.repository.PhotoRepository
import com.rafabap.flickergallery.domain.usecase.PhotoUseCase
import com.rafabap.flickergallery.presentation.util.connection.InternetConnectionVerifier
import com.rafabap.flickergallery.presentation.view.viewmodel.PhotoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelTestModules = module() {
    viewModel { PhotoListViewModel(get(), get()) }
}

val serviceTestModules = module() {
    single {
        FlickerGalleryApi()
    }
    single {
        PhotoRepository(get(), get(), get()) as IPhotoRepository
    }
    single {
        FakeFlickerGalleryApiRestService() as IFlickerGalleryApiRestService
    }
}

val mapperTestModules = module() {
    single {
        PhotoSizeMapper()
    }
}

val useCaseTestModules = module() {
    single {
        PhotoUseCase(get(), get())
    }
}

val providerTestModules = module {
    single {
        FakeSessionPreferencesStorage() as SessionPreferences
    }
    single {
        FakeInternetConnectionVerifier() as InternetConnectionVerifier
    }
}

