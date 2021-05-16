package com.example.android.besthdwallpapers.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.besthdwallpapers.model.remote.response.CommonPic

class PicPagingSource(val repository: Repository, private val query: String) :
    PagingSource<Int, CommonPic>() {
    override fun getRefreshKey(state: PagingState<Int, CommonPic>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommonPic> {
        return try {
            val nextPage = params.key ?: 1
            val response = repository.getPictures(query, nextPage)
            LoadResult.Page(
                response,
                if (nextPage == 1) null else nextPage - 1,
                if (nextPage < response.size) nextPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}