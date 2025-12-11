
package com.example.planner.data.network

import com.example.planner.data.dto.employee.EmployeeInviteDto
import com.example.planner.data.dto.employee.EmployeeResponseDto
import com.example.planner.data.dto.employee.EmployeeUpdateRoleDto
import com.example.planner.data.dto.oauth.AuthRequestDto
import com.example.planner.data.dto.oauth.AuthResponseDto
import com.example.planner.data.dto.oauth.RegisterRequestDto
import com.example.planner.data.dto.oauth.RegisterResponseDto
import com.example.planner.data.dto.project.ProjectCreateRequestDto
import com.example.planner.data.dto.project.ProjectCreateResponseDto
import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.repo.ProjectRepoFileDto
import com.example.planner.data.dto.task.CreateTaskInfoRequestDto
import com.example.planner.data.dto.task.TaskDetailsInfo
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.task.TaskInfoResponseDto
import com.example.planner.data.dto.task.UpdateTaskInfoRequestDto
import com.example.planner.data.dto.tracking.AllTrackingResponse
import com.example.planner.data.dto.tracking.TrackingCreationDto
import com.example.planner.data.dto.tracking.TrackingResponseDto
import com.example.planner.data.dto.userInfo.UserInfoPatchDto
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- Auth ---
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<RegisterResponseDto>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: AuthRequestDto): Response<AuthResponseDto>

    // --- User Info ---
    @GET("api/v1/profile")
    suspend fun getProfileInfo(): Response<UserInfoResponseDto>

    @Multipart
    @PATCH("api/v1/profile")
    suspend fun updateProfile(
        @Part("userInfoPatchDto") userInfo: UserInfoPatchDto,
        @Part image: MultipartBody.Part?
    ): Response<UserInfoResponseDto>

    // --- Projects ---
    @POST("api/v1/project")
    suspend fun createProject(@Body request: ProjectCreateRequestDto): Response<ProjectCreateResponseDto>

    @GET("api/v1/project")
    suspend fun getProjects(): Response<List<ProjectListingDto>>

    @GET("api/v1/project/{projectId}")
    suspend fun getProject(@Path("projectId") projectId: Long): Response<ProjectCreateResponseDto>

    @DELETE("api/v1/project/{projectId}")
    suspend fun deleteProject(@Path("projectId") projectId: Long): Response<Unit>

    // --- Employees ---
    @GET("api/v1/project/{projectId}/employee")
    suspend fun getProjectEmployees(@Path("projectId") projectId: Long): Response<List<EmployeeResponseDto>>

    @POST("api/v1/project/{projectId}/employee")
    suspend fun inviteEmployee(
        @Path("projectId") projectId: Long,
        @Body employeeInviteDto: EmployeeInviteDto
    ): Response<EmployeeResponseDto>

    @DELETE("api/v1/project/{projectId}/employee/{employeeId}")
    suspend fun deleteEmployee(
        @Path("projectId") projectId: Long,
        @Path("employeeId") employeeId: Long
    ): Response<String>

    @PATCH("api/v1/project/{projectId}/employee/{employeeId}")
    suspend fun updateEmployeeRole(
        @Path("projectId") projectId: Long,
        @Path("employeeId") employeeId: Long,
        @Body roleDto: EmployeeUpdateRoleDto
    ): Response<EmployeeResponseDto>

    // --- Tasks ---
    @GET("api/v1/project/{projectId}/task")
    suspend fun getProjectTasks(@Path("projectId") projectId: Long): Response<List<TaskInfoListing>>

    @POST("api/v1/project/{projectId}/task")
    suspend fun createTask(
        @Path("projectId") projectId: Long,
        @Body request: CreateTaskInfoRequestDto
    ): Response<TaskInfoResponseDto>

    @GET("api/v1/project/{projectId}/task/{taskId}")
    suspend fun getTask(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long
    ): Response<TaskInfoResponseDto>

    @PATCH("api/v1/project/{projectId}/task/{taskId}")
    suspend fun updateTask(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long,
        @Body request: UpdateTaskInfoRequestDto
    ): Response<TaskInfoResponseDto>

    @DELETE("api/v1/project/{projectId}/task/{taskId}")
    suspend fun deleteTask(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long
    ): Response<Unit>

    @GET("api/v1/project/{projectId}/task/{taskId}/details")
    suspend fun getTaskDetails(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long
    ): Response<TaskDetailsInfo>

    // --- Repository files ---
    @Multipart
    @POST("api/v1/project/{projectId}/repository")
    suspend fun uploadRepoFile(
        @Path("projectId") projectId: Long,
        @Part file: MultipartBody.Part
    ): Response<String>

    @GET("api/v1/project/{projectId}/repository")
    suspend fun getRepoFiles(@Path("projectId") projectId: Long): Response<List<ProjectRepoFileDto>>

    @GET("api/v1/project/{projectId}/repository/file/{fileId}")
    suspend fun downloadRepoFile(
        @Path("projectId") projectId: Long,
        @Path("fileId") fileId: Long
    ): Response<ByteArray>

    // --- Tracking ---
    @GET("api/v1/project/{projectId}/task/{taskId}/tracking")
    suspend fun getTaskTracking(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long
    ): Response<AllTrackingResponse>

    @POST("api/v1/project/{projectId}/task/{taskId}/tracking")
    suspend fun createTracking(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long,
        @Body request: TrackingCreationDto
    ): Response<TrackingResponseDto>

    @DELETE("api/v1/project/{projectId}/task/{taskId}/tracking/{trackingId}")
    suspend fun deleteTracking(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long,
        @Path("trackingId") trackingId: Long
    ): Response<Unit>
}
