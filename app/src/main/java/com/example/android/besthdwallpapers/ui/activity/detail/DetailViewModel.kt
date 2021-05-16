package com.example.android.besthdwallpapers.ui.activity.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.besthdwallpapers.model.local.Favourite
import com.example.android.besthdwallpapers.model.remote.response.CommonPic
import com.example.android.besthdwallpapers.repository.Repository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    val isFavourite = MutableLiveData<Boolean>()

    fun isFavourite(url: String?) {
        viewModelScope.launch { isFavourite.postValue(repository.isFavourite(url)) }
    }

    fun addorRemoveFromFavourites(pic: CommonPic?) {
        viewModelScope.launch {
            when (isFavourite.value) {
                true -> {
                    repository.removeFromFavourites(pic?.url)
                }
                false -> {
                    repository.addToFavourites(Favourite(pic?.url ?: "", Gson().toJson(pic)))
                }
            }
        }
    }
}