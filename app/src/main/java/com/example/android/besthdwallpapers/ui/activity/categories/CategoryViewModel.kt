package com.example.android.besthdwallpapers.ui.activity.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.model.remote.response.Category
import com.example.android.besthdwallpapers.repository.Repository
import com.example.android.besthdwallpapers.util.isNetworkAvailable
import kotlinx.coroutines.launch

class CategoryViewModel(private val app: Application, private val repository: Repository) :
    AndroidViewModel(app) {

    val categories = MutableLiveData<List<Category>>()
    val progressIsVisible = MutableLiveData<Boolean>().apply { value = true }

    init {
        if (app.baseContext.isNetworkAvailable()) {
            viewModelScope.launch {
                val names = app.baseContext.resources.getStringArray(R.array.categories_array)
                val pictures = mutableListOf<Category>()

                with(repository) {
                    getPictures(names[0], 1)[0].apply { pictures.add(Category(names[0], url)) }
                    getPictures(names[1], 1)[0].apply { pictures.add(Category(names[1], url)) }
                    getPictures(names[2], 1)[0].apply { pictures.add(Category(names[2], url)) }
                    getPictures(names[3], 1)[0].apply { pictures.add(Category(names[3], url)) }
                    getPictures(names[4], 1)[0].apply { pictures.add(Category(names[4], url)) }
                    getPictures(names[5], 1)[0].apply { pictures.add(Category(names[5], url)) }
                }
                categories.postValue(pictures)
                progressIsVisible.postValue(true)
            }
        }
    }
}