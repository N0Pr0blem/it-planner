package com.example.planner.di

import com.example.planner.domain.repository.ProjectRepository
import com.example.planner.domain.usecase.CreateProjectUseCase
import com.example.planner.domain.usecase.CreateTaskUseCase
import com.example.planner.domain.usecase.DeleteEmployeeUseCase
import com.example.planner.domain.usecase.DeleteProjectUseCase
import com.example.planner.domain.usecase.DeleteTaskUseCase
import com.example.planner.domain.usecase.GetProjectEmployeesUseCase
import com.example.planner.domain.usecase.GetProjectTasksUseCase
import com.example.planner.domain.usecase.GetProjectsUseCase
import com.example.planner.domain.usecase.GetTaskUseCase
import com.example.planner.domain.usecase.InviteEmployeeUseCase
import com.example.planner.domain.usecase.UpdateEmployeeRoleUseCase
import com.example.planner.domain.usecase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetProjectsUseCase(repo: ProjectRepository) = GetProjectsUseCase(repo)

    @Provides
    @Singleton
    fun provideCreateProjectUseCase(repo: ProjectRepository) = CreateProjectUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteProjectUseCase(repo: ProjectRepository) = DeleteProjectUseCase(repo)

    @Provides
    @Singleton
    fun provideGetProjectTasksUseCase(repo: ProjectRepository) = GetProjectTasksUseCase(repo)

    @Provides
    @Singleton
    fun provideGetTaskUseCase(repo: ProjectRepository) = GetTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideCreateTaskUseCase(repo: ProjectRepository) = CreateTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(repo: ProjectRepository) = UpdateTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(repo: ProjectRepository) = DeleteTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideGetProjectEmployeesUseCase(repo: ProjectRepository) = GetProjectEmployeesUseCase(repo)

    @Provides
    @Singleton
    fun provideInviteEmployeeUseCase(repo: ProjectRepository) = InviteEmployeeUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteEmployeeUseCase(repo: ProjectRepository) = DeleteEmployeeUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateEmployeeRoleUseCase(repo: ProjectRepository) = UpdateEmployeeRoleUseCase(repo)
}
