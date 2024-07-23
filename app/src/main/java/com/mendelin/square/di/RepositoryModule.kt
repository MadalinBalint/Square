package com.mendelin.square.di

import com.mendelin.square.data.remote.GithubApi
import com.mendelin.square.data.repository.GithubRepositoryImpl
import com.mendelin.square.domain.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGithubRepository(api: GithubApi): GithubRepository =
        GithubRepositoryImpl(api)
}
