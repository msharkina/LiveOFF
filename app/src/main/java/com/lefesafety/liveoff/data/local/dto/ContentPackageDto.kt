package com.lefesafety.liveoff.data.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class ContentPackageDto(
    val packageId: String,
    val version: Int,
    val categories: List<CategoryDto>,
    val cards: List<CardDto>,
    val checklists: List<ChecklistDto>
)

@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
    val icon: String,
    val accentColor: String,
    val sortOrder: Int
)

@Serializable
data class CardDto(
    val id: String,
    val categoryId: String,
    val title: String,
    val briefSteps: List<String>,
    val detailedContent: String,
    val warnings: List<String>,
    val difficulty: String,
    val estimatedTime: String,
    val tools: List<String>,
    val tags: List<String>,
    val version: Int
)

@Serializable
data class ChecklistItemDto(
    val text: String,
    val sortOrder: Int
)

@Serializable
data class ChecklistDto(
    val id: String,
    val categoryId: String,
    val title: String,
    val items: List<ChecklistItemDto>
)
