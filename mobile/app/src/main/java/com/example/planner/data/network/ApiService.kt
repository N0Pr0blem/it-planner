
package com.example.planner.data.network

import com.example.planner.data.dto.*
import com.example.planner.data.dto.employee.EmployeeResponseDto
import com.example.planner.data.dto.oauth.AuthRequestDto
import com.example.planner.data.dto.oauth.AuthResponseDto
import com.example.planner.data.dto.oauth.RegisterRequestDto
import com.example.planner.data.dto.oauth.RegisterResponseDto
import com.example.planner.data.dto.project.ProjectCreateRequestDto
import com.example.planner.data.dto.project.ProjectCreateResponseDto
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import retrofit2.Response
import retrofit2.http.*interface ApiService {

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<RegisterResponseDto>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: AuthRequestDto): Response<AuthResponseDto>

    // --- User Info Controller ---

    @GET("api/v1/profile")
    suspend fun getProfileInfo(): Response<UserInfoResponseDto> // Добавьте обработку токена авторизации!

    // --- Project Controller ---

    @POST("api/v1/project")
    suspend fun createProject(@Body request: ProjectCreateRequestDto): Response<ProjectCreateResponseDto>

    // --- Employee Controller ---

    @GET("api/v1/project/{projectId}/employee")
    suspend fun getProjectEmployees(@Path("projectId") projectId: Long): Response<List<EmployeeResponseDto>>

    // ... и так далее для всех остальных эндпоинтов
}
