package com.lefesafety.liveoff.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.dao.UserDataDao
import com.lefesafety.liveoff.data.local.entity.CardEntity
import com.lefesafety.liveoff.data.local.entity.CardFtsEntity
import com.lefesafety.liveoff.data.local.entity.CategoryEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistEntity
import com.lefesafety.liveoff.data.local.entity.ChecklistProgressEntity
import com.lefesafety.liveoff.data.local.entity.FavoriteEntity
import com.lefesafety.liveoff.data.local.entity.ReadStatusEntity
import com.lefesafety.liveoff.data.local.entity.UserNoteEntity

@Database(
    entities = [
        CategoryEntity::class,
        CardEntity::class,
        CardFtsEntity::class,
        ChecklistEntity::class,
        FavoriteEntity::class,
        UserNoteEntity::class,
        ChecklistProgressEntity::class,
        ReadStatusEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LiveOffDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun cardDao(): CardDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun userDataDao(): UserDataDao
}
