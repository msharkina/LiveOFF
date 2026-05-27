package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contentId: String,
    val contentType: String,
    val addedAt: Long
)
