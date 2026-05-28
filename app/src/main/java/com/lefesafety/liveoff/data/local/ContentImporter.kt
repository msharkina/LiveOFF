package com.lefesafety.liveoff.data.local

import android.content.Context
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.dto.ContentPackageDto
import com.lefesafety.liveoff.data.local.entity.CardEntity
import com.lefesafety.liveoff.data.local.entity.CategoryEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentImporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryDao: CategoryDao,
    private val cardDao: CardDao,
    private val checklistDao: ChecklistDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun importIfNeeded() {
        if (categoryDao.count() > 0) return

        val jsonString = context.assets.open("content/base.json")
            .bufferedReader()
            .use { it.readText() }

        val pkg = json.decodeFromString<ContentPackageDto>(jsonString)

        categoryDao.insertAll(pkg.categories.map { cat ->
            CategoryEntity(
                id = cat.id,
                name = cat.name,
                icon = cat.icon,
                accentColor = cat.accentColor,
                sortOrder = cat.sortOrder
            )
        })

        cardDao.insertAll(pkg.cards.map { card ->
            CardEntity(
                id = card.id,
                categoryId = card.categoryId,
                title = card.title,
                briefSteps = json.encodeToString(card.briefSteps),
                detailedContent = card.detailedContent,
                warnings = json.encodeToString(card.warnings),
                difficulty = card.difficulty,
                estimatedTime = card.estimatedTime,
                tools = json.encodeToString(card.tools),
                tags = json.encodeToString(card.tags),
                version = card.version
            )
        })

        checklistDao.insertAll(pkg.checklists.map { cl ->
            val indexedItems = cl.items.mapIndexed { index, item ->
                item.copy(sortOrder = index)
            }
            ChecklistEntity(
                id = cl.id,
                categoryId = cl.categoryId,
                title = cl.title,
                items = json.encodeToString(indexedItems)
            )
        })
    }
}
