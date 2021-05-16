package com.example.android.besthdwallpapers.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favourite: Favourite): Long

    @Query("DELETE FROM favourites WHERE url=:url")
    suspend fun deleteByUrl(url: String?)

    @Query("DELETE FROM favourites")
    suspend fun deleteAll()

    @Query("SELECT * from favourites WHERE url LIKE :url")
    suspend fun getByUrl(url: String?): List<Favourite>

    @Query("SELECT * from favourites")
    suspend fun getAll(): List<Favourite>

    @Query("SELECT EXISTS(SELECT * from favourites WHERE url = :url)")
    suspend fun isFavourite(url: String?):Boolean
}