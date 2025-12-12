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
    
    // Task Use Cases
    val getProjectTasksUseCase: com.example.planner.domain.usecase.GetProjectTasksUseCase by lazy {
        com.example.planner.domain.usecase.GetProjectTasksUseCase(projectRepository)
    }
    
    val getTaskUseCase: com.example.planner.domain.usecase.GetTaskUseCase by lazy {
        com.example.planner.domain.usecase.GetTaskUseCase(projectRepository)
    }
    
    val createTaskUseCase: com.example.planner.domain.usecase.CreateTaskUseCase by lazy {
        com.example.planner.domain.usecase.CreateTaskUseCase(projectRepository)
    }
    
    val updateTaskUseCase: com.example.planner.domain.usecase.UpdateTaskUseCase by lazy {
        com.example.planner.domain.usecase.UpdateTaskUseCase(projectRepository)
    }
    
    val deleteTaskUseCase: com.example.planner.domain.usecase.DeleteTaskUseCase by lazy {
        com.example.planner.domain.usecase.DeleteTaskUseCase(projectRepository)
    }
    
    // Employee Use Cases
    val getProjectEmployeesUseCase: com.example.planner.domain.usecase.GetProjectEmployeesUseCase by lazy {
        com.example.planner.domain.usecase.GetProjectEmployeesUseCase(projectRepository)
    }
    
    val inviteEmployeeUseCase: com.example.planner.domain.usecase.InviteEmployeeUseCase by lazy {
        com.example.planner.domain.usecase.InviteEmployeeUseCase(projectRepository)
    }
    
    val deleteEmployeeUseCase: com.example.planner.domain.usecase.DeleteEmployeeUseCase by lazy {
        com.example.planner.domain.usecase.DeleteEmployeeUseCase(projectRepository)
    }
    
    val updateEmployeeRoleUseCase: com.example.planner.domain.usecase.UpdateEmployeeRoleUseCase by lazy {
        com.example.planner.domain.usecase.UpdateEmployeeRoleUseCase(projectRepository)
    }
}