package com.example.planner.data.repository

import com.example.planner.data.dto.employee.EmployeeInviteDto
import com.example.planner.data.dto.employee.EmployeeUpdateRoleDto
import com.example.planner.data.dto.project.ProjectCreateRequestDto
import com.example.planner.data.dto.project.ProjectCreateResponseDto
import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.mapper.toDomain
import com.example.planner.data.mapper.toDomainProjects
import com.example.planner.data.mapper.toDomainTasks
import com.example.planner.data.model.user.ProjectRole
import com.example.planner.data.network.RetrofitInstance
import com.example.planner.domain.exception.NetworkException
import com.example.planner.domain.model.Project
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.User
import com.example.planner.domain.repository.ProjectRepository

class ProjectRepositoryImpl : ProjectRepository {
    private val api = RetrofitInstance.api

    override suspend fun getProjects(): Result<List<Project>> {
        return try {
            val response = api.getProjects()
            if (response.isSuccessful) {
                Result.success(response.body()?.toDomainProjects() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load projects"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error occurred", e))
        }
    }

    override suspend fun getProject(projectId: Long): Result<Project> {
        return try {
            val response = api.getProject(projectId)
            if (response.isSuccessful) {
                response.body()?.let { projectDto ->
                    Result.success(Project(
                        id = projectId, // Используем переданный projectId
                        name = projectDto.name,
                        createdAt = projectDto.creationDate.toString(),
                        updatedAt = ""  // TODO: добавить дату обновления
                    ))
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load project"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createProject(name: String): Result<Project> {
        return try {
            val response = api.createProject(ProjectCreateRequestDto(name))
            if (response.isSuccessful) {
                response.body()?.let { projectDto ->
                    // В ProjectCreateResponseDto отсутствует id, поэтому используем фиктивный id
                    // В реальном приложении нужно добавить id в ответ от сервера
                    Result.success(Project(
                        id = 0, // Фиктивный id, так как в DTO нет реального id
                        name = projectDto.name,
                        createdAt = projectDto.creationDate.toString(),
                        updatedAt = ""  // Дата обновления отсутствует в DTO
                    ))
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create project"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProject(projectId: Long): Result<Unit> {
        return try {
            val response = api.deleteProject(projectId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete project"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Tasks
    override suspend fun getProjectTasks(projectId: Long): Result<List<Task>> {
        return try {
            val response = api.getProjectTasks(projectId)
            if (response.isSuccessful) {
                Result.success(response.body()?.toDomainTasks() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load tasks"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTask(projectId: Long, taskId: Long): Result<Task> {
        return try {
            val response = api.getTask(projectId, taskId)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it.toDomain())
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTask(
        projectId: Long,
        name: String,
        description: String,
        urgency: String,
        complexity: String
    ): Result<Task> {
        return try {
            val request = CreateTaskInfoRequestDto(
                name = name,
                urgency = urgency,
                complexity = complexity,
                projectId = projectId,
                description = description
            )
            val response = api.createTask(projectId, request)
            if (response.isSuccessful) {
                response.body()?.let { taskDto ->
                    Result.success(taskDto.toDomain())
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(
        projectId: Long,
        taskId: Long,
        name: String?,
        urgency: String?,
        complexity: String?,
        status: String?,
        description: String?
    ): Result<Task> {
        return try {
            val request = UpdateTaskInfoRequestDto(
                name = name,
                urgency = urgency,
                complexity = complexity,
                status = status,
                description = description
            )
            val response = api.updateTask(projectId, taskId, request)
            if (response.isSuccessful) {
                response.body()?.let { taskDto ->
                    Result.success(taskDto.toDomain())
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(projectId: Long, taskId: Long): Result<Unit> {
        return try {
            val response = api.deleteTask(projectId, taskId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Employees
    override suspend fun getProjectEmployees(projectId: Long): Result<List<User>> {
        return try {
            val response = api.getProjectEmployees(projectId)
            if (response.isSuccessful) {
                val employees = response.body()?.toDomainUsers() ?: emptyList()
                Result.success(employees)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load employees"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun inviteEmployee(projectId: Long, username: String, role: String): Result<User> {
        return try {
            val response = api.inviteEmployee(projectId, EmployeeInviteDto(username, ProjectRole.valueOf(role)))
            if (response.isSuccessful) {
                response.body()?.let { employeeDto ->
                    Result.success(employeeDto.toDomain())
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to invite employee"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteEmployee(projectId: Long, employeeId: Long): Result<Unit> {
        return try {
            val response = api.deleteEmployee(projectId, employeeId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete employee"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateEmployeeRole(projectId: Long, employeeId: Long, role: String): Result<User> {
        return try {
            val response = api.updateEmployeeRole(projectId, employeeId, EmployeeUpdateRoleDto(ProjectRole.valueOf(role)))
            if (response.isSuccessful) {
                response.body()?.let { employeeDto ->
                    Result.success(employeeDto.toDomain())
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update employee role"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
