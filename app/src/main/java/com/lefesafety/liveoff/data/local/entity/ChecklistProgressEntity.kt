package com.lefesafety.liveoff.data.local.entity

import androidx.room.Entity

@Entity(tableName = "checklist_progress", primaryKeys = ["checklistId", "itemIndex"])
data class ChecklistProgressEntity(
    val checklistId: String,
    val itemIndex: Int,
    val isChecked: Boolean
)
