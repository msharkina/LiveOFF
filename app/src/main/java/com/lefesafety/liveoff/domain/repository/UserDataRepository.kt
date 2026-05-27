package com.lefesafety.liveoff.domain.repository

import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.model.Favorite
import com.lefesafety.liveoff.domain.model.UserNote
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    fun getAllFavorites(): Flow<List<Favorite>>
    suspend fun isFavorite(contentId: String, contentType: ContentType): Boolean
    suspend fun toggleFavorite(contentId: String, contentType: ContentType)
    fun observeNote(contentId: String): Flow<UserNote?>
    fun getAllNotes(): Flow<List<UserNote>>
    suspend fun saveNote(contentId: String, text: String)
    suspend fun deleteNote(id: Long)
    fun getChecklistProgress(checklistId: String): Flow<Map<Int, Boolean>>
    suspend fun setChecklistItemChecked(checklistId: String, itemIndex: Int, isChecked: Boolean)
    suspend fun resetChecklistProgress(checklistId: String)
    suspend fun markCardAsRead(cardId: String)
    suspend fun isCardRead(cardId: String): Boolean
    fun getAllReadCardIds(): Flow<Set<String>>
}
