package com.lefesafety.liveoff.domain.model
data class Card(
    val id: String,
    val categoryId: String,
    val title: String,
    val briefSteps: List<String>,
    val detailedContent: String,
    val warnings: List<String>,
    val difficulty: Difficulty,
    val estimatedTime: String,
    val tools: List<String>,
    val tags: List<String>,
    val version: Int
)
