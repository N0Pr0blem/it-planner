
package com.example.planner.data.network

import com.example.planner.data.dto.MessageResponseDto
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
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.task.TaskInfoResponseDto
import com.example.planner.data.dto.task.TaskFileDto
import com.example.planner.data.dto.task.UpdateTaskInfoRequestDto
import com.example.planner.data.dto.tracking.AllTrackingResponse
import com.example.planner.data.dto.tracking.TrackingCreationDto
import com.example.planner.data.dto.tracking.TrackingResponseDto
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- Auth ---
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<RegisterResponseDto>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: AuthRequestDto): Response<AuthResponseDto>

    @POST("api/v1/auth/verify")
    suspend fun verify(
        @Query("code") code: String,
        @Query("username") username: String
    ): Response<MessageResponseDto>

    @POST("api/v1/auth/resend")
    suspend fun resendVerificationCode(
        @Query("username") username: String
    ): Response<MessageResponseDto>

    // --- User Info ---
    @GET("api/v1/profile")
    suspend fun getProfileInfo(): Response<UserInfoResponseDto>

    @Multipart
    @PATCH("api/v1/profile")
    suspend fun updateProfile(
        @Part("secondName") secondName: RequestBody?,
        @Part("lastName") lastName: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<UserInfoResponseDto>

    // --- Projects ---
    @POST("api/v1/project")
    suspend fun createProject(@Body request: ProjectCreateRequestDto): Response<ProjectCreateResponseDto>

    @GET("api/v1/project")
    suspend fun getProjects(): Response<List<ProjectListingDto?>>

    @GET("api/v1/project/my")
    suspend fun getMyProjects(): Response<List<ProjectListingDto?>>

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
    @GET("api/v1/project/{projectId}/browse")
    suspend fun getProjectTasks(@Path("projectId") projectId: Long): Response<List<TaskInfoListing>>

    @POST("api/v1/task")
    suspend fun createTask(
        @Body request: CreateTaskInfoRequestDto
    ): Response<TaskInfoResponseDto>

    @GET("api/v1/task/{taskId}")
    suspend fun getTask(
        @Path("taskId") taskId: Long
    ): Response<TaskInfoResponseDto>

    @PATCH("api/v1/task/{taskId}")
    suspend fun updateTask(
        @Path("taskId") taskId: Long,
        @Body request: UpdateTaskInfoRequestDto
    ): Response<TaskInfoResponseDto>

    @DELETE("api/v1/task/{taskId}")
    suspend fun deleteTask(
        @Path("taskId") taskId: Long
    ): Response<Unit>

    @GET("api/v1/task/{taskId}/description")
    suspend fun getTaskDetails(
        @Path("taskId") taskId: Long
    ): Response<MessageResponseDto>

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
    ): Response<ResponseBody>

    // --- Task files ---
    @Multipart
    @POST("api/v1/task/{taskId}/file")
    suspend fun uploadTaskFile(
        @Path("taskId") taskId: Long,
        @Part file: MultipartBody.Part
    ): Response<MessageResponseDto>

    @GET("api/v1/task/{taskId}/file")
    suspend fun getTaskFiles(@Path("taskId") taskId: Long): Response<List<TaskFileDto>>

    @GET("api/v1/task/{taskId}/file/{fileId}")
    suspend fun downloadTaskFile(
        @Path("taskId") taskId: Long,
        @Path("fileId") fileId: Long
    ): Response<ResponseBody>

    @DELETE("api/v1/task/{taskId}/file/{fileId}")
    suspend fun deleteTaskFile(
        @Path("taskId") taskId: Long,
        @Path("fileId") fileId: Long
    ): Response<MessageResponseDto>

    @PATCH("api/v1/project/{projectId}/task/{taskId}/employee/{employeeId}")
    suspend fun assignTaskToEmployee(
        @Path("projectId") projectId: Long,
        @Path("taskId") taskId: Long,
        @Path("employeeId") employeeId: Long
    ): Response<String>

    @DELETE("api/v1/project/{projectId}/repository/file/{fileId}")
    suspend fun deleteRepoFile(
        @Path("projectId") projectId: Long,
        @Path("fileId") fileId: Long
    ): Response<Unit>

    // --- Tracking ---
    @GET("api/v1/task/{taskId}/trekking")
    suspend fun getTaskTracking(
        @Path("taskId") taskId: Long
    ): Response<AllTrackingResponse>

    @POST("api/v1/trekking")
    suspend fun createTracking(
        @Body request: TrackingCreationDto
    ): Response<TrackingResponseDto>

    @DELETE("api/v1/trekking/{trackingId}")
    suspend fun deleteTracking(
        @Path("trackingId") trackingId: Long
    ): Response<Unit>
}
