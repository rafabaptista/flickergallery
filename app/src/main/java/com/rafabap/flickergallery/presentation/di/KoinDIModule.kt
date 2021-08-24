package com.rafabap.flickergallery.base

import com.rafabap.flickergallery.data.mapper.PhotoSizeMapper
import com.rafabap.flickergallery.data.network.api.FlickerGalleryApi
import com.rafabap.flickergallery.data.network.storage.SessionPreferences
import com.rafabap.flickergallery.data.network.storage.SessionPreferencesStorage
import com.rafabap.flickergallery.domain.repository.IPhotoRepository
import com.rafabap.flickergallery.domain.repository.PhotoRepository
import com.rafabap.flickergallery.domain.usecase.PhotoUseCase
import com.rafabap.flickergallery.presentation.util.connection.DefaultInternetConnectionVerifier
import com.rafabap.flickergallery.presentation.util.connection.InternetConnectionVerifier
import com.rafabap.flickergallery.presentation.view.viewmodel.PhotoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module() {
    viewModel { PhotoListViewModel(get(), get()) }
}

val serviceModules = module() {
    single {
        FlickerGalleryApi()
    }
    single {
        PhotoRepository(get(), get(), get()) as IPhotoRepository
    }
}

val mapperModules = module() {
    single {
        PhotoSizeMapper()
    }
}

val useCaseModules = module() {
    single {
        PhotoUseCase(get(), get())
    }
}

val providerModules = module {
    single { SessionPreferencesStorage(get()) as SessionPreferences }
    single { DefaultInternetConnectionVerifier(get()) as InternetConnectionVerifier }
}
