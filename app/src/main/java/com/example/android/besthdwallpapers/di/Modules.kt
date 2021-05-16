package com.example.android.besthdwallpapers.di

import android.content.Context
import com.example.android.besthdwallpapers.model.local.FavDatabase
import com.example.android.besthdwallpapers.model.remote.ApiClient
import com.example.android.besthdwallpapers.repository.Repository
import com.example.android.besthdwallpapers.ui.activity.categories.CategoryViewModel
import com.example.android.besthdwallpapers.ui.activity.detail.DetailViewModel
import com.example.android.besthdwallpapers.ui.activity.favourites.FavouriteViewModel
import com.example.android.besthdwallpapers.ui.activity.main.MainViewModel
import com.example.android.besthdwallpapers.ui.fragment.GalleryViewModel
import com.example.android.besthdwallpapers.util.Constants.MAIN_STORAGE
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module { single { ApiClient.create(get()) } }

val dbModule = module { single { FavDatabase.buildDefault(get()).dao() } }

val repositoryModule = module {
    single { Repository(get(), get()) }
}
val preferenceModule = module {
    single {
        androidApplication().getSharedPreferences(
            MAIN_STORAGE,
            Context.MODE_PRIVATE
        )
    }
}

val viewModelModule = module(override = true) {
    viewModel { MainViewModel(get()) }
    viewModel { GalleryViewModel(get()) }
    viewModel { CategoryViewModel(androidApplication(), get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { FavouriteViewModel(get()) }
}