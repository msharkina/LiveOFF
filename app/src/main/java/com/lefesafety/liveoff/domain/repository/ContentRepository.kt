package com.lefesafety.liveoff.domain.repository

import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.Checklist
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    fun getCardsByCategory(categoryId: String): Flow<List<Card>>
    fun getCardById(id: String): Flow<Card?>
    fun getAllCards(): Flow<List<Card>>
    fun searchCards(query: String): Flow<List<Card>>
    fun getChecklistsByCategory(categoryId: String): Flow<List<Checklist>>
    fun getChecklistById(id: String): Flow<Checklist?>
    fun getAllChecklists(): Flow<List<Checklist>>
    suspend fun importContent()
}
