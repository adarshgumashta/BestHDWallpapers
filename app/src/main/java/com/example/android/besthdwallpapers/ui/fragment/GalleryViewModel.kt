package com.example.android.besthdwallpapers.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.besthdwallpapers.model.remote.response.CommonPic
import com.example.android.besthdwallpapers.repository.PicPagingSource
import com.example.android.besthdwallpapers.repository.Repository
import kotlinx.coroutines.flow.Flow

class GalleryViewModel(private val repository: Repository) : ViewModel() {

    fun getPicListStream(query: String): Flow<PagingData<CommonPic>> {
        return Pager(PagingConfig(40)) {
            PicPagingSource(repository, query)
        }.flow
    }
}