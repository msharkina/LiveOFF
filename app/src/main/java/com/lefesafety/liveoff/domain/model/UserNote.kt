package com.lefesafety.liveoff.domain.model
data class UserNote(
    val id: Long = 0,
    val contentId: String,
    val text: String,
    val updatedAt: Long
)
