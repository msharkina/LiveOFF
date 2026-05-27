package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklists",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class ChecklistEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val title: String,
    val items: String
)
