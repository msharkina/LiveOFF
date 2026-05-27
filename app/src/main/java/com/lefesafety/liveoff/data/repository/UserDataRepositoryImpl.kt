package com.lefesafety.liveoff.data.repository

import com.lefesafety.liveoff.data.local.dao.UserDataDao
import com.lefesafety.liveoff.data.local.entity.ChecklistProgressEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.ReadStatusEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity
import com.lefesafety.liveoff.data.local.toDomain
import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.model.Favorite
import com.lefesafety.liveoff.domain.model.UserNote
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepositoryImpl @Inject constructor(
    private val userDataDao: UserDataDao
) : UserDataRepository {
    override fun getAllFavorites(): Flow<List<Favorite>> =
        userDataDao.getAllFavorites().map { list -> list.map { it.toDomain() } }
    override suspend fun isFavorite(contentId: String, contentType: ContentType): Boolean =
        userDataDao.getFavorite(contentId, contentType.name) != null
    override suspend fun toggleFavorite(contentId: String, contentType: ContentType) {
        val existing = userDataDao.getFavorite(contentId, contentType.name)
        if (existing != null) {
            userDataDao.deleteFavorite(contentId, contentType.name)
        } else {
            userDataDao.insertFavorite(FavoriteEntity(contentId = contentId, contentType = contentType.name, addedAt = System.currentTimeMillis()))
        }
    }
    override fun observeNote(contentId: String): Flow<UserNote?> =
        userDataDao.observeNote(contentId).map { it?.toDomain() }
    override fun getAllNotes(): Flow<List<UserNote>> =
        userDataDao.getAllNotes().map { list -> list.map { it.toDomain() } }
    override suspend fun saveNote(contentId: String, text: String) {
        userDataDao.upsertNote(UserNoteEntity(contentId = contentId, text = text, updatedAt = System.currentTimeMillis()))
    }
    override suspend fun deleteNote(id: Long) { userDataDao.deleteNote(id) }
    override fun getChecklistProgress(checklistId: String): Flow<Map<Int, Boolean>> =
        userDataDao.getChecklistProgress(checklistId).map { list -> list.associate { it.itemIndex to it.isChecked } }
    override suspend fun setChecklistItemChecked(checklistId: String, itemIndex: Int, isChecked: Boolean) {
        userDataDao.upsertChecklistProgress(ChecklistProgressEntity(checklistId = checklistId, itemIndex = itemIndex, isChecked = isChecked))
    }
    override suspend fun resetChecklistProgress(checklistId: String) { userDataDao.resetChecklistProgress(checklistId) }
    override suspend fun markCardAsRead(cardId: String) {
        userDataDao.markAsRead(ReadStatusEntity(cardId = cardId, readAt = System.currentTimeMillis()))
    }
    override suspend fun isCardRead(cardId: String): Boolean = userDataDao.getReadStatus(cardId) != null
    override fun getAllReadCardIds(): Flow<Set<String>> =
        userDataDao.getAllReadStatuses().map { list -> list.map { it.cardId }.toSet() }
}
