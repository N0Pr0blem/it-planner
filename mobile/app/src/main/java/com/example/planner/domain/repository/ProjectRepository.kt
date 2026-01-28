package com.example.planner.domain.repository

import com.example.planner.domain.model.Project
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.ProjectMember
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskUrgency

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
        urgency: TaskUrgency,
        complexity: TaskComplexity
    ): Result<Task>
    
    suspend fun updateTask(
        projectId: Long,
        taskId: Long,
        name: String? = null,
        urgency: TaskUrgency? = null,
        complexity: TaskComplexity? = null,
        status: String? = null,
        description: String? = null
    ): Result<Task>
    
    suspend fun deleteTask(projectId: Long, taskId: Long): Result<Unit>
    
    // Employees
    suspend fun getProjectEmployees(projectId: Long): Result<List<ProjectMember>>
    suspend fun inviteEmployee(projectId: Long, username: String, role: ProjectRole): Result<ProjectMember>
    suspend fun deleteEmployee(projectId: Long, employeeId: Long): Result<Unit>
    suspend fun updateEmployeeRole(projectId: Long, employeeId: Long, role: ProjectRole): Result<ProjectMember>
}
