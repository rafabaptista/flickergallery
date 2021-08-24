package com.rafabap.flickergallery.data.repository

import com.rafabap.flickergallery.data.model.PhotoResponse
import com.rafabap.flickergallery.data.model.SizeResponse
import com.rafabap.flickergallery.data.network.api.FlickerGalleryApi
import com.rafabap.flickergallery.data.network.service.FakeInternetConnectionVerifier
import com.rafabap.flickergallery.data.network.storage.SessionPreferences
import com.rafabap.flickergallery.data.storage.FakeSessionPreferencesStorage
import com.rafabap.flickergallery.domain.repository.IPhotoRepository
import com.rafabap.flickergallery.domain.repository.PhotoRepository
import com.rafabap.flickergallery.presentation.util.TrampolineSchedulerRule
import com.rafabap.flickergallery.presentation.util.connection.InternetConnectionVerifier
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import io.reactivex.rxjava3.core.Observable
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import kotlin.test.assertNotNull


class PhotoRepositoryTest : KoinTest {

    @get:Rule
    var schedulersOverrideRule: TrampolineSchedulerRule = TrampolineSchedulerRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { FakeSessionPreferencesStorage() as SessionPreferences }
                single { FakeInternetConnectionVerifier() as InternetConnectionVerifier }
                single { FlickerGalleryApi() }
                single { PhotoRepository(get(), get(), get()) as IPhotoRepository }
            })
    }

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

    @Test
    fun `should inject my components`() {
        val mockApi = declareMock<FlickerGalleryApi>()
        val mockStorage = declareMock<FakeSessionPreferencesStorage>()
        val mockInternetConnection = declareMock<FakeInternetConnectionVerifier>()
        val mockRepository = declareMock<PhotoRepository>()
        assertNotNull(get<FlickerGalleryApi>())
        assertNotNull(get<FakeSessionPreferencesStorage>())
        assertNotNull(get<FakeInternetConnectionVerifier>())
        assertNotNull(get<PhotoRepository>())
    }

    @Test
    fun `fetch photos with load from cache initial`() {
        val mockApi = declareMock<FlickerGalleryApi>() {
            given(searchPhotoWithSize(anyInt())).willReturn(Observable.just(photoResponse))
        }
        val mockStorage = declareMock<FakeSessionPreferencesStorage>() {
            given(loadPhotoData()).willReturn(Observable.just(photoResponse, photoResponse2))
        }
        val mockInternetConnection = declareMock<FakeInternetConnectionVerifier>() {
            given(isConnectedToInternet()).willReturn(true)
        }
        PhotoRepository(mockApi, mockStorage, mockInternetConnection).fetchSearchPhoto().test()
        Mockito.verify(mockApi, times(1)).searchPhotoWithSize(anyInt())
        Mockito.verify(mockStorage, times(1)).loadPhotoData()
        Mockito.verify(mockStorage, times(2)).savePhotoData(any())
    }

    @Test
    fun `fetch photos with pagination`() {
        val mockApi = declareMock<FlickerGalleryApi>() {
            given(searchPhotoWithSize(anyInt())).willReturn(Observable.just(photoResponse))
        }
        val mockStorage = declareMock<FakeSessionPreferencesStorage>() {
            given(loadPhotoData()).willReturn(Observable.just(photoResponse, photoResponse2))
        }
        val mockInternetConnection = declareMock<FakeInternetConnectionVerifier>() {
            given(isConnectedToInternet()).willReturn(true)
        }
        PhotoRepository(mockApi, mockStorage, mockInternetConnection).fetchSearchPhotoByPage(2).test()
        Mockito.verify(mockApi, times(1)).searchPhotoWithSize(anyInt())
    }
}