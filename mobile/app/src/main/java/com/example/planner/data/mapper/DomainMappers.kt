package com.example.planner.data.mapper

import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import com.example.planner.domain.model.Project
import com.example.planner.domain.model.ProjectMember
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.model.User
import com.example.planner.data.model.user.ProjectRole as DataProjectRole

// User mappers
fun UserInfoResponseDto.toDomain(): User {
    return User(
        id = 0, // Используем 0 как фиктивный id, так как в DTO нет id
        username = username,
        firstName = firstName,
        secondName = secondName,
        lastName = lastName,
        email = null, // Email отсутствует в DTO, можно добавить позже
        profileImageUrl = profileImage
    )
}

// Project mappers
fun ProjectListingDto.toDomain(): Project {
    return Project(
        id = id,
        name = name,
        createdAt = "", // Даты создания и обновления отсутствуют в DTO
        updatedAt = ""
    )
}

fun List<ProjectListingDto>.toDomainProjects(): List<Project> {
    return map { it.toDomain() }
}

// Employee mappers
fun com.example.planner.data.dto.employee.EmployeeResponseDto.toDomainMember(): ProjectMember {
    return ProjectMember(
        id = id,
        role = projectRole.toDomain(),
        firstName = user.firstName,
        secondName = user.secondName,
        profileImageUrl = user.profileImage
    )
}

fun com.example.planner.data.dto.employee.EmployeeResponseDto.toDomain(): User {
    return User(
        id = id,
        username = "${user.firstName ?: ""} ${user.secondName ?: ""}".trim(),
        firstName = user.firstName,
        secondName = user.secondName,
        lastName = null,
        email = null,
        profileImageUrl = user.profileImage
    )
}

fun List<com.example.planner.data.dto.employee.EmployeeResponseDto>.toDomainUsers(): List<User> {
    return map { it.toDomain() }
}
fun List<com.example.planner.data.dto.employee.EmployeeResponseDto>.toDomainMembers(): List<ProjectMember> {
    return map { it.toDomainMember() }
}

fun DataProjectRole.toDomain(): ProjectRole {
    return when (this) {
        DataProjectRole.PROJECT_MANAGER -> ProjectRole.PROJECT_MANAGER
        DataProjectRole.FRONTEND_DEVELOPER -> ProjectRole.FRONTEND_DEVELOPER
        DataProjectRole.BACKEND_DEVELOPER -> ProjectRole.BACKEND_DEVELOPER
        DataProjectRole.TESTER -> ProjectRole.TESTER
        DataProjectRole.UI_UX_DESIGNER -> ProjectRole.UI_UX_DESIGNER
        DataProjectRole.DEVOPS -> ProjectRole.DEVOPS
        DataProjectRole.ANOTHER -> ProjectRole.ANOTHER
    }
}

fun ProjectRole.toData(): DataProjectRole {
    return when (this) {
        ProjectRole.PROJECT_MANAGER -> DataProjectRole.PROJECT_MANAGER
        ProjectRole.FRONTEND_DEVELOPER -> DataProjectRole.FRONTEND_DEVELOPER
        ProjectRole.BACKEND_DEVELOPER -> DataProjectRole.BACKEND_DEVELOPER
        ProjectRole.TESTER -> DataProjectRole.TESTER
        ProjectRole.UI_UX_DESIGNER -> DataProjectRole.UI_UX_DESIGNER
        ProjectRole.DEVOPS -> DataProjectRole.DEVOPS
        ProjectRole.ANOTHER -> DataProjectRole.ANOTHER
    }
}
