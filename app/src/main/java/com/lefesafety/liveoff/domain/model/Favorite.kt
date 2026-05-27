package com.lefesafety.liveoff.domain.model
data class Favorite(
    val id: Long = 0,
    val contentId: String,
    val contentType: ContentType,
    val addedAt: Long
)
