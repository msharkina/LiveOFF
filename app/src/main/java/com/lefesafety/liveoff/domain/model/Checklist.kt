package com.lefesafety.liveoff.domain.model
data class ChecklistItem(
    val text: String,
    val sortOrder: Int
)
data class Checklist(
    val id: String,
    val categoryId: String,
    val title: String,
    val items: List<ChecklistItem>
)
