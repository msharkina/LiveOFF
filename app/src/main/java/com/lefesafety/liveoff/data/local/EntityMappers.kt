package com.lefesafety.liveoff.data.local

import com.lefesafety.liveoff.data.local.entity.CardEntity
import com.lefesafety.liveoff.data.local.entity.CategoryEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.Checklist
import com.lefesafety.liveoff.domain.model.ChecklistItem
import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.model.Difficulty
import com.lefesafety.liveoff.domain.model.Favorite
import com.lefesafety.liveoff.domain.model.UserNote
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun CategoryEntity.toDomain() = Category(
    id = id, name = name, icon = icon, accentColor = accentColor, sortOrder = sortOrder
)

fun CardEntity.toDomain() = Card(
    id = id,
    categoryId = categoryId,
    title = title,
    briefSteps = json.decodeFromString<List<String>>(briefSteps),
    detailedContent = detailedContent,
    warnings = json.decodeFromString<List<String>>(warnings),
    difficulty = Difficulty.valueOf(difficulty),
    estimatedTime = estimatedTime,
    tools = json.decodeFromString<List<String>>(tools),
    tags = json.decodeFromString<List<String>>(tags),
    version = version
)

fun ChecklistEntity.toDomain(): Checklist {
    @Serializable
    data class ItemDto(val text: String, val sortOrder: Int = 0)
    val itemDtos = json.decodeFromString<List<ItemDto>>(items)
    return Checklist(
        id = id, categoryId = categoryId, title = title,
        items = itemDtos.map { ChecklistItem(text = it.text, sortOrder = it.sortOrder) }
    )
}

fun FavoriteEntity.toDomain() = Favorite(
    id = id, contentId = contentId, contentType = ContentType.valueOf(contentType), addedAt = addedAt
)

fun UserNoteEntity.toDomain() = UserNote(
    id = id, contentId = contentId, text = text, updatedAt = updatedAt
)
