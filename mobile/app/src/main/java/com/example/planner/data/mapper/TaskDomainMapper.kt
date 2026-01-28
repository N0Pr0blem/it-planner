package com.example.planner.data.mapper

import com.example.planner.data.dto.task.TaskFileDto
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.task.TaskInfoResponseDto
import com.example.planner.data.dto.tracking.AllTrackingResponse
import com.example.planner.data.dto.tracking.TrackingResponseDto
import com.example.planner.data.model.task.TaskComplexity as DataComplexity
import com.example.planner.data.model.task.TaskStatus as DataStatus
import com.example.planner.data.model.task.TaskUrgency as DataUrgency
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskFile
import com.example.planner.domain.model.TaskStatus
import com.example.planner.domain.model.TaskUrgency
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.TrackingRecord
import com.example.planner.domain.model.TrackingSummary
import com.example.planner.domain.model.User

private fun DataStatus.toDomain(): TaskStatus = when (this) {
    DataStatus.TO_DO -> TaskStatus.TO_DO
    DataStatus.IN_PROGRESS -> TaskStatus.IN_PROGRESS
    DataStatus.REVIEW -> TaskStatus.REVIEW
    DataStatus.IN_TEST -> TaskStatus.IN_TEST
    DataStatus.DONE -> TaskStatus.DONE
}

private fun DataUrgency.toDomain(): TaskUrgency = when (this) {
    DataUrgency.URGENT -> TaskUrgency.URGENT
    DataUrgency.MEDIUM -> TaskUrgency.MEDIUM
    DataUrgency.NOT_URGENT -> TaskUrgency.NOT_URGENT
}

private fun DataComplexity.toDomain(): TaskComplexity = when (this) {
    DataComplexity.HARD -> TaskComplexity.HARD
    DataComplexity.MEDIUM -> TaskComplexity.MEDIUM
    DataComplexity.EASY -> TaskComplexity.EASY
}

fun TaskInfoResponseDto.toDomain(): Task = Task(
    id = id,
    projectId = projectId,
    name = name,
    description = description.orEmpty(),
    status = status.toDomain(),
    urgency = urgency.toDomain(),
    complexity = complexity.toDomain(),
    isCompleted = isCompleted,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
    assignedBy = assignedBy?.toDomainUser(),
    assignedTo = assignedTo?.toDomainUser()
)

fun TaskInfoListing.toDomain(projectId: Long = 0L): Task = Task(
    id = id,
    projectId = projectId,
    name = name,
    description = "",
    status = status?.let { safe ->
        runCatching { TaskStatus.valueOf(safe) }.getOrElse { if (isCompleted) TaskStatus.DONE else TaskStatus.TO_DO }
    } ?: if (isCompleted) TaskStatus.DONE else TaskStatus.TO_DO,
    urgency = TaskUrgency.MEDIUM,
    complexity = TaskComplexity.MEDIUM,
    isCompleted = isCompleted,
    createdAt = "",
    updatedAt = "",
    assignedBy = null,
    assignedTo = null
)

fun List<TaskInfoListing>.toDomainTasks(projectId: Long = 0L): List<Task> =
    map { it.toDomain(projectId) }

private fun com.example.planner.data.dto.userInfo.UserInfoResponseDto.toDomainUser(): User =
    User(
        id = id ?: 0L,
        username = username ?: "",
        firstName = firstName,
        secondName = secondName,
        lastName = lastName,
        email = email,
        profileImageUrl = profileImage
    )

private fun com.example.planner.data.dto.userInfo.UserInfoForTaskDto.toDomainUser(): User =
    User(
        id = id ?: 0L,
        username = username ?: "",
        firstName = firstName,
        secondName = secondName,
        lastName = lastName,
        email = email,
        profileImageUrl = profileImage
    )

fun TaskFileDto.toDomain(): TaskFile =
    TaskFile(
        id = id ?: 0L,
        taskId = taskId,
        name = name ?: "",
        type = type?.name
    )

fun TrackingResponseDto.toDomain(): TrackingRecord =
    TrackingRecord(
        id = id,
        date = date,
        hours = hours,
        employeeFirstName = employeeFirstName,
        employeeSecondName = employeeSecondName,
        taskDetailsId = taskDetailsId
    )

fun AllTrackingResponse.toDomain(): TrackingSummary =
    TrackingSummary(
        records = trekkingList.map { it.toDomain() },
        totalHours = hourSum
    )
