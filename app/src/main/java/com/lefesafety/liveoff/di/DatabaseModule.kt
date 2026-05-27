package com.lefesafety.liveoff.di

import android.content.Context
import androidx.room.Room
import com.lefesafety.liveoff.data.local.dao.CardDao
import com.lefesafety.liveoff.data.local.dao.CategoryDao
import com.lefesafety.liveoff.data.local.dao.ChecklistDao
import com.lefesafety.liveoff.data.local.dao.UserDataDao
import com.lefesafety.liveoff.data.local.db.LiveOffDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LiveOffDatabase =
        Room.databaseBuilder(context, LiveOffDatabase::class.java, "liveoff.db")
            .build()

    @Provides fun provideCategoryDao(db: LiveOffDatabase): CategoryDao = db.categoryDao()
    @Provides fun provideCardDao(db: LiveOffDatabase): CardDao = db.cardDao()
    @Provides fun provideChecklistDao(db: LiveOffDatabase): ChecklistDao = db.checklistDao()
    @Provides fun provideUserDataDao(db: LiveOffDatabase): UserDataDao = db.userDataDao()
}
