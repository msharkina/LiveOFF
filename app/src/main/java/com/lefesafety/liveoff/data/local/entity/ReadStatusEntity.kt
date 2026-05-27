package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_status")
data class ReadStatusEntity(
    @PrimaryKey val cardId: String,
    val readAt: Long
)
