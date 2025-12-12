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
        id = 0, // TODO: добавить id в DTO
        username = username,
        firstName = firstName,
        secondName = secondName,
        lastName = lastName,
        email = null, // TODO: добавить email в DTO
        profileImageUrl = profileImage
    )
}

// Project mappers
fun ProjectListingDto.toDomain(): Project {
    return Project(
        id = id,
        name = name,
        createdAt = "", // TODO: добавить даты в DTO
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
        projectId = 0, // TODO: добавить projectId в DTO
        name = name,
        description = "", // TODO: добавить описание
        status = if (isCompleted) TaskStatus.DONE else TaskStatus.TO_DO,
        urgency = TaskUrgency.MEDIUM, // TODO: добавить приоритет
        complexity = TaskComplexity.MEDIUM, // TODO: добавить сложность
        isCompleted = isCompleted,
        createdAt = "", // TODO: добавить даты
        updatedAt = "",
        assignedBy = null, // TODO: добавить назначенного
        assignedTo = null
    )
}

fun TaskInfoResponseDto.toDomain(): Task {
    return Task(
        id = id,
        projectId = projectId,
        name = name,
        description = "", // TODO: получить описание из деталей задачи
        status = TaskStatus.valueOf(status),
        urgency = TaskUrgency.valueOf(urgency),
        complexity = TaskComplexity.valueOf(complexity),
        isCompleted = isCompleted,
        createdAt = creationDate.toString(),
        updatedAt = "", // TODO: добавить updatedAt
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
        id = 0, // TODO: добавить id
        username = "", // TODO: добавить username
        firstName = firstName,
        secondName = secondName,
        lastName = null,
        email = null,
        profileImageUrl = null
    )
}