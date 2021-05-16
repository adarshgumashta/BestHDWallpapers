package com.example.android.besthdwallpapers.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.besthdwallpapers.model.local.DatabaseMigration.V1.Favourite.tableName

@Entity(tableName = tableName)
data class Favourite(
    @PrimaryKey var url: String,
    @ColumnInfo(name = Favourite.Column.hit) var hit: String?
) {
    object Favourite {
        const val tableName = "favourites"

        object Column {
            const val url = "url"
            const val hit = "hit"
        }
    }
}