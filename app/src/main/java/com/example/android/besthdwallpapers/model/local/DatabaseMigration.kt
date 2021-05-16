package com.example.android.besthdwallpapers.model.local

import androidx.room.migration.Migration

internal object DatabaseMigration {

    const val latestVersion = 1

    val allMigrations: Array<Migration>
        get() = arrayOf()

    object V1 {
        object Favourite {
            const val tableName = "favourites"

            object Column {
                const val id = "id"
                const val hit = "hit"
            }
        }
    }
}