package com.example.planner.di

import com.example.planner.data.repository.ProjectRepositoryImpl
import com.example.planner.domain.repository.ProjectRepository
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
    fun provideProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository = impl
}
