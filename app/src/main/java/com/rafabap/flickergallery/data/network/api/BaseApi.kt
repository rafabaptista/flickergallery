package com.rafabap.flickergallery.data.network.api

import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


open class BaseApi {

    companion object {
        private const val API_TIMEOUT_SECONDS = 30
        private const val API_KEY = "9a95c68a9c6ec61104cd3967dcbb8bd3" //TODO extract to external buildconfig
    }

    fun build(timeout: Int = API_TIMEOUT_SECONDS): Retrofit {
        val baseUrl =
            "https://api.flickr.com/services/rest/" //TODO extract to external buildconfig

        val builder = OkHttpClient.Builder()
            .addInterceptor(generalInterceptor())
            .addInterceptor(logInterceptor())


        val clientBuilder = builder
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)


        val client = clientBuilder.build()
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )

        return retrofitBuilder.build()
    }

    private fun generalInterceptor(): Interceptor =
        invoke { chain ->
            val original = chain.request()

            val originalHttpUrl = original.url

            val url: HttpUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val builder = original.newBuilder()
                .url(url)
                .header("Content-Type", "application/json")
                .method(original.method, original.body)

            val request = builder.build()
            chain.proceed(request)
        }

    private fun logInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}