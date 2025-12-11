package com.example.planner.data.repository

import com.example.planner.data.dto.employee.EmployeeInviteDto
import com.example.planner.data.dto.employee.EmployeeResponseDto
import com.example.planner.data.dto.employee.EmployeeUpdateRoleDto
import com.example.planner.data.dto.project.ProjectCreateRequestDto
import com.example.planner.data.dto.project.ProjectCreateResponseDto
import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.repo.ProjectRepoFileDto
import com.example.planner.data.model.user.ProjectRole
import com.example.planner.data.network.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProjectRepository {
    private val api = RetrofitInstance.api

    suspend fun getProjects(): Result<List<ProjectListingDto>> {
        return try {
            val response = api.getProjects()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load projects"))
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
    suspend fun getRepoFiles(projectId: Long): Result<List<ProjectRepoFileDto>> {
        return try {
            val response = api.getRepoFiles(projectId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load files"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadRepoFile(projectId: Long, file: File): Result<String> {
        return try {
            val requestBody = file.asRequestBody()
            val part = MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
            val response = api.uploadRepoFile(projectId, part)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "File uploaded")
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to upload file"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadRepoFile(projectId: Long, fileId: Long): Result<ByteArray> {
        return try {
            val response = api.downloadRepoFile(projectId, fileId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty file"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to download file"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
