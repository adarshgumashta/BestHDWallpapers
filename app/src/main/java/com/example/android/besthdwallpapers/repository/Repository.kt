package com.example.android.besthdwallpapers.repository

import com.example.android.besthdwallpapers.BuildConfig.UNSPLASH_URL
import com.example.android.besthdwallpapers.model.local.FavDao
import com.example.android.besthdwallpapers.model.local.Favourite
import com.example.android.besthdwallpapers.model.remote.ApiService
import com.example.android.besthdwallpapers.model.remote.response.CommonPic

class Repository(private val service: ApiService, private val dao: FavDao) {
    suspend fun getPictures(query: String, page: Int): List<CommonPic> {
        val resUnSplash = service.getUnSplashPictures(UNSPLASH_URL, query, page)
        val resPixaBay = service.getPixabayPictures(query, page)

        val listUnSplash = resUnSplash.body()?.results?.map {
            CommonPic(
                url = it.urls?.small ?: "",
                width = it.width ?: 0,
                height = it.height ?: 0,
                tags = it.altDescription,
                imageURL = it.urls?.full,
                fullHDURL = it.urls?.regular,
                id = it.hashCode(),
                videoId = ""
            )
        }

        val listPixaBay = resPixaBay.body()?.hits?.map {
            CommonPic(
                url = it.webformatURL ?: "",
                width = it.imageWidth,
                height = it.imageHeight,
                tags = it.tags,
                imageURL = it.imageURL,
                fullHDURL = it.webformatURL,
                id = it.id,
                videoId = ""
            )
        }

        val list = mutableListOf<CommonPic>()
        listUnSplash?.let { list.addAll(it) }
        listPixaBay?.let {
            it.forEach { if (it.id != 158703 && it.id != 158704) list.add(it) }

        }
        return list.shuffled()
    }

    suspend fun addToFavourites(favourite: Favourite) = dao.insert(favourite)

    suspend fun removeFromFavourites(url: String?) = dao.deleteByUrl(url)

    suspend fun deleteAll() = dao.deleteAll()
    suspend fun getByUrl(url: String?) = dao.getByUrl(url)

    suspend fun getFavourite() = dao.getAll()

    suspend fun isFavourite(url: String?) = dao.isFavourite(url)
}