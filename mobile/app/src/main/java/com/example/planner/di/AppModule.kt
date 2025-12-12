package com.example.planner.di

import com.example.planner.data.repository.ProjectRepositoryImpl
import com.example.planner.domain.repository.ProjectRepository
import com.example.planner.domain.usecase.CreateProjectUseCase
import com.example.planner.domain.usecase.GetProjectsUseCase

object AppModule {
    
    // Репозитории
    val projectRepository: ProjectRepository by lazy {
        ProjectRepositoryImpl()
    }
    
    // Use Cases
    val getProjectsUseCase: GetProjectsUseCase by lazy {
        GetProjectsUseCase(projectRepository)
    }
    
    val createProjectUseCase: CreateProjectUseCase by lazy {
        CreateProjectUseCase(projectRepository)
    }
    
    // TODO: добавить другие use cases и репозитории
}