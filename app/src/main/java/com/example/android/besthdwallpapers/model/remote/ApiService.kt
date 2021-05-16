package com.example.android.besthdwallpapers.model.remote

import com.example.android.besthdwallpapers.BuildConfig.PIXABAY_KEY

import com.example.android.besthdwallpapers.model.remote.response.pixabay.Pic
import com.example.android.besthdwallpapers.model.remote.response.unsplash.UnsplashResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("?key=$PIXABAY_KEY")
    suspend fun getPixabayPictures(
        @Query("q") query: String,
        @Query("page") index: Int
    ): Response<Pic>

    @GET
    suspend fun getUnSplashPictures(
        @Url url: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<UnsplashResponse>
}