package com.lefesafety.liveoff.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lefesafety.liveoff.data.local.entity.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards WHERE categoryId = :categoryId")
    fun getCardsByCategory(categoryId: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCardById(id: String): CardEntity?

    @Query("SELECT * FROM cards WHERE id = :id")
    fun observeCard(id: String): Flow<CardEntity?>

    @Query("""
        SELECT cards.* FROM cards
        JOIN cards_fts ON cards.rowid = cards_fts.rowid
        WHERE cards_fts MATCH :query
    """)
    fun search(query: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<CardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardEntity>)

    @Query("SELECT COUNT(*) FROM cards")
    suspend fun count(): Int
}
