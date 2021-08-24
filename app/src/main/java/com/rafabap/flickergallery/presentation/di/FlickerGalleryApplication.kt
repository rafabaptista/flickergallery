package com.rafabap.flickergallery.presentation.di

import android.app.Application
import com.rafabap.flickergallery.base.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module

class FlickerGalleryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FlickerGalleryApplication)
            modules(appComponent)
        }
    }

    private val appComponent: List<Module> = listOf(
        viewModelModules,
        providerModules,
        useCaseModules,
        serviceModules,
        mapperModules
    )
}