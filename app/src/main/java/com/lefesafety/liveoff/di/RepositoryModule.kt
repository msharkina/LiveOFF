package com.lefesafety.liveoff.di

import com.lefesafety.liveoff.data.repository.ContentRepositoryImpl
import com.lefesafety.liveoff.data.repository.UserDataRepositoryImpl
import com.lefesafety.liveoff.domain.repository.ContentRepository
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindContentRepository(impl: ContentRepositoryImpl): ContentRepository
    @Binds @Singleton abstract fun bindUserDataRepository(impl: UserDataRepositoryImpl): UserDataRepository
}
