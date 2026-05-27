package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class CardEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val title: String,
    val briefSteps: String,
    val detailedContent: String,
    val warnings: String,
    val difficulty: String,
    val estimatedTime: String,
    val tools: String,
    val tags: String,
    val version: Int
)
