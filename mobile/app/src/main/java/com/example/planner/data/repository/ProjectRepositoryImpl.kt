package com.example.planner.data.repository

import com.example.planner.data.dto.employee.EmployeeInviteDto
import com.example.planner.data.dto.employee.EmployeeUpdateRoleDto
import com.example.planner.data.dto.project.ProjectCreateRequestDto
import com.example.planner.data.dto.project.ProjectCreateResponseDto
import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.task.CreateTaskInfoRequestDto
import com.example.planner.data.dto.task.UpdateTaskInfoRequestDto
import com.example.planner.data.mapper.toDomain
import com.example.planner.data.mapper.toData
import com.example.planner.data.mapper.toDomainMember
import com.example.planner.data.mapper.toDomainMembers
import com.example.planner.data.mapper.toDomainProjects
import com.example.planner.data.mapper.toDomainTasks
import com.example.planner.data.network.ApiService
import com.example.planner.di.CacheModule
import com.example.planner.domain.exception.NetworkException
import com.example.planner.domain.model.Project
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.ProjectMember
import com.example.planner.domain.model.User
import com.example.planner.domain.model.TaskComplexity as DomainComplexity
import com.example.planner.domain.model.TaskUrgency as DomainUrgency
import com.example.planner.domain.model.TaskStatus as DomainStatus
import com.example.planner.domain.repository.ProjectRepository
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ProjectRepository {

    override suspend fun getProjects(): Result<List<Project>> {
        // Check cache first
        val cachedProjects = CacheModule.getCachedProjects()
        if (cachedProjects != null) {
            return Result.success(cachedProjects)
        }
        
        return try {
            val primary = api.getProjects()
            if (primary.isSuccessful) {
                val projects = primary.body()
                    ?.filterNotNull()
                    ?.map { it.toDomain() }
                    ?: emptyList()
                CacheModule.cacheProjects(projects)
                return Result.success(projects)
            }

            // fallback: some deployments serve projects at /api/v1/project/my
            val fallback = api.getMyProjects()
            if (fallback.isSuccessful) {
                val projects = fallback.body()
                    ?.filterNotNull()
                    ?.map { it.toDomain() }
                    ?: emptyList()
                CacheModule.cacheProjects(projects)
                Result.success(projects)
            } else {
                Result.failure(Exception(fallback.errorBody()?.string() ?: "Failed to load projects"))
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
                    val project = Project(
                        id = 0, // Фиктивный id, так как в DTO нет реального id
                        name = projectDto.name,
                        createdAt = projectDto.creationDate.toString(),
                        updatedAt = ""  // Дата обновления отсутствует в DTO
                    )
                    // Clear projects cache after creation
                    CacheModule.clearProjectsCache()
                    Result.success(project)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create project"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error occurred", e))
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
        // Check cache first
        val cachedTasks = CacheModule.getCachedTasks(projectId)
        if (cachedTasks != null) {
            return Result.success(cachedTasks)
        }
        
        return try {
            val response = api.getProjectTasks(projectId)
            if (response.isSuccessful) {
                val tasks = response.body()?.toDomainTasks() ?: emptyList()
                // Cache the results
                CacheModule.cacheTasks(projectId, tasks)
                Result.success(tasks)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load tasks"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error occurred", e))
        }
    }

    override suspend fun getTask(projectId: Long, taskId: Long): Result<Task> {
        return try {
            val response = api.getTask(taskId)
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
        urgency: DomainUrgency,
        complexity: DomainComplexity
    ): Result<Task> {
        return try {
            val urgencyEnum = when (urgency) {
                DomainUrgency.URGENT -> com.example.planner.data.model.task.TaskUrgency.URGENT
                DomainUrgency.MEDIUM -> com.example.planner.data.model.task.TaskUrgency.MEDIUM
                DomainUrgency.NOT_URGENT -> com.example.planner.data.model.task.TaskUrgency.NOT_URGENT
            }
            val complexityEnum = when (complexity) {
                DomainComplexity.HARD -> com.example.planner.data.model.task.TaskComplexity.HARD
                DomainComplexity.MEDIUM -> com.example.planner.data.model.task.TaskComplexity.MEDIUM
                DomainComplexity.EASY -> com.example.planner.data.model.task.TaskComplexity.EASY
            }
            
            val request = CreateTaskInfoRequestDto(
                name = name,
                urgency = urgencyEnum, // Use actual enum object
                complexity = complexityEnum, // Use actual enum object
                projectId = projectId,
                description = description
            )
            val response = api.createTask(request)
            if (response.isSuccessful) {
                response.body()?.let { taskDto ->
                    CacheModule.clearTasksCache(projectId)
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
        urgency: DomainUrgency?,
        complexity: DomainComplexity?,
        status: String?,
        description: String?
    ): Result<Task> {
        return try {
            val urgencyEnum = urgency?.let {
                when (it) {
                    DomainUrgency.URGENT -> com.example.planner.data.model.task.TaskUrgency.URGENT
                    DomainUrgency.MEDIUM -> com.example.planner.data.model.task.TaskUrgency.MEDIUM
                    DomainUrgency.NOT_URGENT -> com.example.planner.data.model.task.TaskUrgency.NOT_URGENT
                }
            }

            val complexityEnum = complexity?.let {
                when (it) {
                    DomainComplexity.HARD -> com.example.planner.data.model.task.TaskComplexity.HARD
                    DomainComplexity.MEDIUM -> com.example.planner.data.model.task.TaskComplexity.MEDIUM
                    DomainComplexity.EASY -> com.example.planner.data.model.task.TaskComplexity.EASY
                }
            }

            val statusEnum = status?.let {
                when (it.uppercase()) {
                    "TO_DO" -> com.example.planner.data.model.task.TaskStatus.TO_DO
                    "IN_PROGRESS" -> com.example.planner.data.model.task.TaskStatus.IN_PROGRESS
                    "REVIEW" -> com.example.planner.data.model.task.TaskStatus.REVIEW
                    "IN_TEST" -> com.example.planner.data.model.task.TaskStatus.IN_TEST
                    "DONE" -> com.example.planner.data.model.task.TaskStatus.DONE
                    else -> com.example.planner.data.model.task.TaskStatus.TO_DO
                }
            }
            
            val request = UpdateTaskInfoRequestDto(
                name = name,
                urgency = urgencyEnum, // Use actual enum object
                complexity = complexityEnum, // Use actual enum object
                status = statusEnum, // Use actual enum object
                description = description
            )
            val response = api.updateTask(taskId, request)
            if (response.isSuccessful) {
                response.body()?.let { taskDto ->
                    CacheModule.clearTasksCache(projectId)
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
            val response = api.deleteTask(taskId)
            if (response.isSuccessful) {
                CacheModule.clearTasksCache(projectId)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Employees
    override suspend fun getProjectEmployees(projectId: Long): Result<List<ProjectMember>> {
        // Check cache first
        val cachedUsers = CacheModule.getCachedUsers(projectId)
        if (cachedUsers != null) {
            return Result.success(cachedUsers)
        }
        
        return try {
            val response = api.getProjectEmployees(projectId)
            if (response.isSuccessful) {
                val employees = response.body()?.toDomainMembers() ?: emptyList()
                // Cache the results
                CacheModule.cacheUsers(projectId, employees)
                Result.success(employees)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load employees"))
            }
        } catch (e: Exception) {
            Result.failure(NetworkException("Network error occurred", e))
        }
    }

    override suspend fun inviteEmployee(projectId: Long, username: String, role: ProjectRole): Result<ProjectMember> {
        return try {
            val response = api.inviteEmployee(projectId, EmployeeInviteDto(username, role.toData()))
            if (response.isSuccessful) {
                CacheModule.clearUsersCache(projectId)
                response.body()?.let { employeeDto ->
                    Result.success(employeeDto.toDomainMember())
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
                CacheModule.clearUsersCache(projectId)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete employee"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateEmployeeRole(projectId: Long, employeeId: Long, role: ProjectRole): Result<ProjectMember> {
        return try {
            val response = api.updateEmployeeRole(projectId, employeeId, EmployeeUpdateRoleDto(role.toData()))
            if (response.isSuccessful) {
                CacheModule.clearUsersCache(projectId)
                response.body()?.let { employeeDto ->
                    Result.success(employeeDto.toDomainMember())
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update employee role"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
