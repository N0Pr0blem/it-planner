package com.example.planner.data.mapper

import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import com.example.planner.domain.model.Project
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.UserProfile

fun ProjectListingDto.toDomain(): Project =
    Project(
        id = id,
        name = name,
        createdAt = "",
        updatedAt = ""
    )

fun UserInfoResponseDto.toDomain(): UserProfile =
    UserProfile(
        username = username,
        firstName = firstName,
        secondName = secondName,
        lastName = lastName,
        profileImage = profileImage,
        registrationDate = registrationDate,
        projects = projects.map { it.toDomain() },
        tasks = tasks.map { it.toDomain(projectId = it.projectId ?: 0L) }
    )
