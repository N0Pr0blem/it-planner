package com.example.planner.di

import com.example.planner.data.repository.AuthRepositoryImpl
import com.example.planner.data.repository.TaskRepositoryImpl
import com.example.planner.data.repository.UserRepositoryImpl
import com.example.planner.data.repository.ProjectRepositoryLegacy
import com.example.planner.domain.repository.AuthRepository
import com.example.planner.domain.repository.TaskRepository
import com.example.planner.domain.repository.UserRepository
import com.example.planner.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindings {

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindRepoRepository(impl: ProjectRepositoryLegacy): RepoRepository
}
