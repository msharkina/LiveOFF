package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_notes")
data class UserNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contentId: String,
    val text: String,
    val updatedAt: Long
)
