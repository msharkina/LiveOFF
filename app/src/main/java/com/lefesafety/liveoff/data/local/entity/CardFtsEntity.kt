package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = CardEntity::class)
@Entity(tableName = "cards_fts")
data class CardFtsEntity(
    val title: String,
    val briefSteps: String,
    val detailedContent: String
)
