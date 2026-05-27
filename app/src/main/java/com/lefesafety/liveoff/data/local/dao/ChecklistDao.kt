package com.lefesafety.liveoff.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklists WHERE categoryId = :categoryId")
    fun getChecklistsByCategory(categoryId: String): Flow<List<ChecklistEntity>>

    @Query("SELECT * FROM checklists WHERE id = :id")
    suspend fun getChecklistById(id: String): ChecklistEntity?

    @Query("SELECT * FROM checklists WHERE id = :id")
    fun observeChecklist(id: String): Flow<ChecklistEntity?>

    @Query("SELECT * FROM checklists")
    fun getAllChecklists(): Flow<List<ChecklistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checklists: List<ChecklistEntity>)

    @Query("SELECT COUNT(*) FROM checklists")
    suspend fun count(): Int
}
