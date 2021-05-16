package com.example.android.besthdwallpapers.model.remote.response.unsplash

import com.example.android.besthdwallpapers.model.remote.response.unsplash.Result
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnsplashResponse {
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
}