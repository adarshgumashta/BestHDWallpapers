package com.example.android.besthdwallpapers.ui.activity.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.besthdwallpapers.model.local.Favourite
import com.example.android.besthdwallpapers.repository.Repository
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: Repository) : ViewModel() {
    val favourites = MutableLiveData<List<Favourite>>()
    val isEmpty = MutableLiveData<Boolean>()

    fun getFavourites() {
        viewModelScope.launch {
            val favs = repository.getFavourite()
            favourites.postValue(favs)
            isEmpty.postValue(favs.isNullOrEmpty())
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
            getFavourites()
        }
    }
}
