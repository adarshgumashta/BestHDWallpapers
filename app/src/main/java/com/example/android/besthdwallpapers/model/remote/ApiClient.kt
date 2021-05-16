package com.example.android.besthdwallpapers.model.remote

import android.content.Context
import com.example.android.besthdwallpapers.BuildConfig.BASE_URL
import com.example.android.besthdwallpapers.BuildConfig.DEBUG
import com.example.android.besthdwallpapers.util.isNetworkAvailable
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiClient {
    fun create(context: Context): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(CacheInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .cache(Cache(File(context.cacheDir, "ResponseCache"), (30 * 1024 * 1024).toLong()))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build()

        return retrofit.create(ApiService::class.java)
    }

    class CacheInterceptor(private val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            if (!context.isNetworkAvailable()) {
                request = request.newBuilder().removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 2419200).build()
            }
            return chain.proceed(request)
        }

    }

}