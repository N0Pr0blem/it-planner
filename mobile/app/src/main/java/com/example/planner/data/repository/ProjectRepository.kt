package com.example.planner.data.repository

import com.example.planner.data.dto.employee.EmployeeInviteDto
import com.example.planner.data.dto.employee.EmployeeResponseDto
import com.example.planner.data.dto.employee.EmployeeUpdateRoleDto
import com.example.planner.data.dto.project.ProjectCreateRequestDto
import com.example.planner.data.dto.project.ProjectCreateResponseDto
import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.repo.ProjectRepoFileDto
import com.example.planner.data.model.user.ProjectRole
import com.example.planner.data.network.ApiService
import com.example.planner.data.mapper.toDomain
import com.example.planner.domain.model.RepoFile
import com.example.planner.domain.repository.RepoRepository
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryLegacy @Inject constructor(
    private val api: ApiService
) : RepoRepository {

    suspend fun getProjects(): Result<List<ProjectListingDto>> {
        return try {
            val primary = api.getProjects()
            if (primary.isSuccessful) {
                val projects = primary.body()?.filterNotNull() ?: emptyList()
                return Result.success(projects)
            }

            // fallback: some backends expose /project/my instead of plain /project
            val fallback = api.getMyProjects()
            if (fallback.isSuccessful) {
                val projects = fallback.body()?.filterNotNull() ?: emptyList()
                Result.success(projects)
            } else {
                Result.failure(Exception(fallback.errorBody()?.string() ?: "Failed to load projects"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProject(projectId: Long): Result<ProjectCreateResponseDto> {
        return try {
            val response = api.getProject(projectId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load project"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProject(name: String): Result<ProjectCreateResponseDto> {
        return try {
            val response = api.createProject(ProjectCreateRequestDto(name))
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create project"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProject(projectId: Long): Result<Unit> {
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

    // Employees
    suspend fun getEmployees(projectId: Long): Result<List<EmployeeResponseDto>> {
        return try {
            val response = api.getProjectEmployees(projectId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load employees"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun inviteEmployee(projectId: Long, username: String, role: ProjectRole): Result<EmployeeResponseDto> {
        return try {
            val response = api.inviteEmployee(projectId, EmployeeInviteDto(username, role))
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to invite employee"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEmployee(projectId: Long, employeeId: Long): Result<Unit> {
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

    suspend fun updateEmployeeRole(projectId: Long, employeeId: Long, role: ProjectRole): Result<EmployeeResponseDto> {
        return try {
            val response = api.updateEmployeeRole(projectId, employeeId, EmployeeUpdateRoleDto(role))
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update employee role"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Repository files
    override suspend fun getRepoFiles(projectId: Long): Result<List<RepoFile>> {
        return try {
            val response = api.getRepoFiles(projectId)
            if (response.isSuccessful) {
                val files = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(files)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load files"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadRepoFile(
        projectId: Long,
        file: File,
        filename: String? = null,
        contentType: String? = null
    ): Result<String> {
        return try {
            val mediaType = contentType?.toMediaTypeOrNull()
            val requestBody = file.asRequestBody(mediaType)
            val safeName = filename?.takeIf { it.isNotBlank() } ?: file.name
            val part = MultipartBody.Part.createFormData("file", safeName, requestBody)
            val response = api.uploadRepoFile(projectId, part)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "File uploaded")
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (!errorBody.isNullOrBlank()) {
                    errorBody
                } else {
                    "HTTP ${response.code()}: Failed to upload file"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadRepoFile(projectId: Long, fileId: Long): Result<ByteArray> {
        return try {
            val response = api.downloadRepoFile(projectId, fileId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.bytes()) }
                    ?: Result.failure(Exception("Empty file"))
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (!errorBody.isNullOrBlank()) {
                    errorBody
                } else {
                    "HTTP ${response.code()}: Failed to download file"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun deleteRepoFile(projectId: Long, fileId: Long): Result<Unit> {
        return try {
            val response = api.deleteRepoFile(projectId, fileId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete file"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
