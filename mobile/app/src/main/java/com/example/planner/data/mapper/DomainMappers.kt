package com.example.planner.data.mapper

import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.task.TaskInfoResponseDto
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import com.example.planner.domain.model.Project
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.User
import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency

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

// Task mappers
fun TaskInfoListing.toDomain(): Task {
    return Task(
        id = id,
        projectId = 0, // projectId отсутствует в DTO, используем 0 как фиктивное значение
        name = name,
        description = "", // Описание отсутствует в DTO
        status = if (isCompleted) TaskStatus.DONE else TaskStatus.TO_DO,
        urgency = TaskUrgency.MEDIUM, // Приоритет отсутствует в DTO, используем средний
        complexity = TaskComplexity.MEDIUM, // Сложность отсутствует в DTO, используем среднюю
        isCompleted = isCompleted,
        createdAt = "", // Даты отсутствуют в DTO
        updatedAt = "",
        assignedBy = null, // Назначенный отсутствует в DTO
        assignedTo = null
    )
}

fun TaskInfoResponseDto.toDomain(): Task {
    return Task(
        id = id,
        projectId = projectId,
        name = name,
        description = "", // Описание нужно получать из деталей задачи (отдельный запрос)
        status = TaskStatus.valueOf(status),
        urgency = TaskUrgency.valueOf(urgency),
        complexity = TaskComplexity.valueOf(complexity),
        isCompleted = isCompleted,
        createdAt = creationDate.toString(),
        updatedAt = "", // Дата обновления отсутствует в DTO
        assignedBy = assignedBy?.toDomain(),
        assignedTo = assignedTo?.toDomain()
    )
}

fun List<TaskInfoListing>.toDomainTasks(): List<Task> {
    return map { it.toDomain() }
}

// Helper mappers for DTO user info
fun com.example.planner.data.dto.userInfo.UserInfoForTaskDto.toDomain(): User {
    return User(
        id = 0, // id отсутствует в DTO
        username = "", // username отсутствует в DTO
        firstName = firstName,
        secondName = secondName,
        lastName = null,
        email = null,
        profileImageUrl = profileImage
    )
}

// Employee mappers
fun com.example.planner.data.dto.employee.EmployeeResponseDto.toDomain(): User {
    return User(
        id = id,
        username = user.username ?: "",
        firstName = user.firstName,
        secondName = user.secondName,
        lastName = null,
        email = null,
        profileImageUrl = null
    )
}

fun List<com.example.planner.data.dto.employee.EmployeeResponseDto>.toDomainUsers(): List<User> {
    return map { it.toDomain() }
}