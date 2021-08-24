package com.rafabap.flickergallery.presentation.viewmodel

import com.rafabap.flickergallery.data.mapper.PhotoSizeMapper
import com.rafabap.flickergallery.data.model.PhotoResponse
import com.rafabap.flickergallery.data.model.SizeResponse
import com.rafabap.flickergallery.data.network.api.FlickerGalleryApi
import com.rafabap.flickergallery.data.network.service.FakeInternetConnectionVerifier
import com.rafabap.flickergallery.data.network.storage.SessionPreferences
import com.rafabap.flickergallery.data.storage.FakeSessionPreferencesStorage
import com.rafabap.flickergallery.domain.model.Photo
import com.rafabap.flickergallery.domain.repository.IPhotoRepository
import com.rafabap.flickergallery.domain.repository.PhotoRepository
import com.rafabap.flickergallery.domain.usecase.PhotoUseCase
import com.rafabap.flickergallery.presentation.util.TrampolineSchedulerRule
import com.rafabap.flickergallery.presentation.util.connection.InternetConnectionVerifier
import com.rafabap.flickergallery.presentation.view.viewmodel.PhotoListViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito
import org.mockito.Mockito
import kotlin.test.assertNotNull

class PhotoListViewModelTest : KoinTest {
    @get:Rule
    var schedulersOverrideRule: TrampolineSchedulerRule = TrampolineSchedulerRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { FakeSessionPreferencesStorage() as SessionPreferences }
                single { FakeInternetConnectionVerifier() as InternetConnectionVerifier }
                single { FlickerGalleryApi() }
                single { PhotoSizeMapper() }
                single { PhotoRepository(get(), get(), get()) as IPhotoRepository }
                single { PhotoUseCase(get(), get()) }
                single { PhotoListViewModel(get(), get()) }
            })
    }

    lateinit var viewModel: PhotoListViewModel

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    private val photoResponse = PhotoResponse(
        id = "id",
        listOf(
            SizeResponse(
                label = "Large Square",
                source = "source"
            ), SizeResponse(
                label = "Large",
                source = "source2"
            )
        )
    )

    private val photoResponse2 = PhotoResponse(
        id = "id2",
        listOf(
            SizeResponse(
                label = "Large Square",
                source = "source"
            ), SizeResponse(
                label = "Large",
                source = "source2"
            )
        )
    )

    private val photo = Photo(
        id = "id",
        normalPhotoUrl = "normalPhotoUrl",
        largePhotoUrl = "largePhotoUrl"
    )

    private val photo2 = Photo(
        id = "id2",
        normalPhotoUrl = "normalPhotoUrl",
        largePhotoUrl = "largePhotoUrl"
    )

    @Test
    fun `should inject my components`() {
        val mockApi = declareMock<FlickerGalleryApi>()
        val mockStorage = declareMock<FakeSessionPreferencesStorage>()
        val mockInternetConnection = declareMock<FakeInternetConnectionVerifier>()
        val photoSizeMapper = declareMock<PhotoSizeMapper>()
        val mockRepository = declareMock<PhotoRepository>()
        val photoUseCase = declareMock<PhotoUseCase>()
        val photoListViewModel = declareMock<PhotoListViewModel>()
        assertNotNull(get<FlickerGalleryApi>())
        assertNotNull(get<FakeSessionPreferencesStorage>())
        assertNotNull(get<FakeInternetConnectionVerifier>())
        assertNotNull(get<PhotoSizeMapper>())
        assertNotNull(get<PhotoRepository>())
        assertNotNull(get<PhotoUseCase>())
        assertNotNull(get<PhotoListViewModel>())
    }

    @Test
    fun `test loaPhotos`() {
        val mockApi = declareMock<FlickerGalleryApi>() {
            BDDMockito.given(searchPhotoWithSize(anyInt())).willReturn(Observable.just(photoResponse))
        }
        val mockStorage = declareMock<FakeSessionPreferencesStorage>() {
            BDDMockito.given(loadPhotoData())
                .willReturn(Observable.just(photoResponse, photoResponse2))
        }
        val mockInternetConnection = declareMock<FakeInternetConnectionVerifier>() {
            BDDMockito.given(isConnectedToInternet()).willReturn(true)
        }
        val mockUseCase = declareMock<PhotoUseCase>()
        val mockRepository = declareMock<PhotoRepository>() {
            BDDMockito.given(fetchSearchPhotoByPage(anyInt()))
                .willReturn(Observable.just(photoResponse, photoResponse2))
        }
        val mockPhotoSizeMapper = declareMock<PhotoSizeMapper>() {
            BDDMockito.given(mapWithSize(any())).willReturn(photo)
        }

        PhotoRepository(mockApi, mockStorage, mockInternetConnection).fetchSearchPhotoByPage(2).test()
        Mockito.verify(mockApi, times(1)).searchPhotoWithSize(anyInt())

        PhotoUseCase(mockRepository, mockPhotoSizeMapper).callSearchApi(2, object :
            Observer<Photo> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(response: Photo?) {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {

            }

        })
        Mockito.verify(mockRepository, times(1)).fetchSearchPhotoByPage(anyInt())

        viewModel = PhotoListViewModel(mockUseCase, mockInternetConnection)
        viewModel.loaPhotos()
        Assert.assertEquals(true, viewModel.hasInternet())
        Mockito.verify(mockUseCase, times(1)).callSearchApi(anyInt(), any())
    }
}