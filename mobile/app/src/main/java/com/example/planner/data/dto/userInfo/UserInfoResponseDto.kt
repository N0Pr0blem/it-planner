package com.example.planner.data.dto.userInfo

import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.task.TaskInfoListing
import java.time.LocalDateTime

data class UserInfoResponseDto(
    val username: String,
    val firstName: String? = null,
    val secondName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null,
    val registrationDate: LocalDateTime? = null,
    val projects: List<ProjectListingDto> = emptyList(),
    val tasks: List<TaskInfoListing> = emptyList()
)