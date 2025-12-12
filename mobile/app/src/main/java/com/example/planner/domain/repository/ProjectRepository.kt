package com.example.planner.domain.repository

import com.example.planner.domain.model.Project
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.User

interface ProjectRepository {
    suspend fun getProjects(): Result<List<Project>>
    suspend fun getProject(projectId: Long): Result<Project>
    suspend fun createProject(name: String): Result<Project>
    suspend fun deleteProject(projectId: Long): Result<Unit>
    
    // Tasks
    suspend fun getProjectTasks(projectId: Long): Result<List<Task>>
    suspend fun getTask(projectId: Long, taskId: Long): Result<Task>
    suspend fun createTask(
        projectId: Long,
        name: String,
        description: String,
        urgency: String,
        complexity: String
    ): Result<Task>
    
    suspend fun updateTask(
        projectId: Long,
        taskId: Long,
        name: String? = null,
        urgency: String? = null,
        complexity: String? = null,
        status: String? = null,
        description: String? = null
    ): Result<Task>
    
    suspend fun deleteTask(projectId: Long, taskId: Long): Result<Unit>
    
    // Employees
    suspend fun getProjectEmployees(projectId: Long): Result<List<User>>
    suspend fun inviteEmployee(projectId: Long, username: String, role: String): Result<User>
    suspend fun deleteEmployee(projectId: Long, employeeId: Long): Result<Unit>
    suspend fun updateEmployeeRole(projectId: Long, employeeId: Long, role: String): Result<User>
}