package com.lefesafety.liveoff.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lefesafety.liveoff.data.local.entity.ChecklistProgressEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.ReadStatusEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE contentId = :contentId AND contentType = :contentType LIMIT 1")
    suspend fun getFavorite(contentId: String, contentType: String): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE contentId = :contentId AND contentType = :contentType")
    suspend fun deleteFavorite(contentId: String, contentType: String)

    @Query("SELECT * FROM user_notes WHERE contentId = :contentId LIMIT 1")
    fun observeNote(contentId: String): Flow<UserNoteEntity?>

    @Query("SELECT * FROM user_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<UserNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: UserNoteEntity)

    @Query("DELETE FROM user_notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query("SELECT * FROM checklist_progress WHERE checklistId = :checklistId")
    fun getChecklistProgress(checklistId: String): Flow<List<ChecklistProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChecklistProgress(progress: ChecklistProgressEntity)

    @Query("DELETE FROM checklist_progress WHERE checklistId = :checklistId")
    suspend fun resetChecklistProgress(checklistId: String)

    @Query("SELECT * FROM read_status WHERE cardId = :cardId LIMIT 1")
    suspend fun getReadStatus(cardId: String): ReadStatusEntity?

    @Query("SELECT * FROM read_status")
    fun getAllReadStatuses(): Flow<List<ReadStatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAsRead(readStatus: ReadStatusEntity)
}
