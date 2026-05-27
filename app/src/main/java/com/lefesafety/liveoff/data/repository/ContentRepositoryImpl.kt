package com.lefesafety.liveoff.data.repository

import com.lefesafety.liveoff.data.local.ContentImporter
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.toDomain
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.Checklist
import com.lefesafety.liveoff.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val cardDao: CardDao,
    private val checklistDao: ChecklistDao,
    private val contentImporter: ContentImporter
) : ContentRepository {
    override fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories().map { list -> list.map { it.toDomain() } }
    override suspend fun getCategoryById(id: String): Category? =
        categoryDao.getCategoryById(id)?.toDomain()
    override fun getCardsByCategory(categoryId: String): Flow<List<Card>> =
        cardDao.getCardsByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    override fun getCardById(id: String): Flow<Card?> =
        cardDao.observeCard(id).map { it?.toDomain() }
    override fun getAllCards(): Flow<List<Card>> =
        cardDao.getAllCards().map { list -> list.map { it.toDomain() } }
    override fun searchCards(query: String): Flow<List<Card>> =
        cardDao.search("$query*").map { list -> list.map { it.toDomain() } }
    override fun getChecklistsByCategory(categoryId: String): Flow<List<Checklist>> =
        checklistDao.getChecklistsByCategory(categoryId).map { list -> list.map { it.toDomain() } }
    override fun getChecklistById(id: String): Flow<Checklist?> =
        checklistDao.observeChecklist(id).map { it?.toDomain() }
    override fun getAllChecklists(): Flow<List<Checklist>> =
        checklistDao.getAllChecklists().map { list -> list.map { it.toDomain() } }
    override suspend fun importContent() { contentImporter.importIfNeeded() }
}
