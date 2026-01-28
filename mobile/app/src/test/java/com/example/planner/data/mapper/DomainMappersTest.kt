package com.example.planner.data.mapper

import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import com.example.planner.domain.model.Project
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.User
import com.example.planner.data.model.task.TaskStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class DomainMappersTest {

    @Test
    fun `UserInfoResponseDto toDomain should map correctly`() {
        // Given
        val dto = UserInfoResponseDto(
            username = "testuser",
            firstName = "Test",
            secondName = "User",
            lastName = "Last",
            profileImage = "profile.jpg",
            registrationDate = LocalDateTime.now()
        )

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(0, result.id)
        assertEquals("testuser", result.username)
        assertEquals("Test", result.firstName)
        assertEquals("User", result.secondName)
        assertEquals("Last", result.lastName)
        assertEquals(null, result.email)
        assertEquals("profile.jpg", result.profileImageUrl)
    }

    @Test
    fun `ProjectListingDto toDomain should map correctly`() {
        // Given
        val dto = ProjectListingDto(1, "Test Project")

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(1, result.id)
        assertEquals("Test Project", result.name)
        assertEquals("", result.createdAt)
        assertEquals("", result.updatedAt)
    }

    @Test
    fun `TaskInfoListing toDomain should map correctly`() {
        // Given
        val dto = TaskInfoListing(
            id = 1,
            name = "Test Task",
            isCompleted = true,
            assignBy = "testuser",
            assignByImage = "profile.jpg"
        )
        val expectedAssignedBy = User(
            id = 0,
            username = "",
            firstName = "testuser",
            secondName = null,
            lastName = null,
            email = null,
            profileImageUrl = "profile.jpg"
        )

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(1, result.id)
        assertEquals(0, result.projectId)
        assertEquals("Test Task", result.name)
        assertEquals("", result.description)
        assertEquals(TaskStatus.DONE, result.status)
        assertEquals(com.example.planner.data.model.task.TaskUrgency.MEDIUM, result.urgency)
        assertEquals(com.example.planner.data.model.task.TaskComplexity.MEDIUM, result.complexity)
        assertEquals(true, result.isCompleted)
        assertEquals("", result.createdAt)
        assertEquals("", result.updatedAt)
        assertEquals(expectedAssignedBy, result.assignedBy)
        assertEquals(null, result.assignedTo)
    }

    @Test
    fun `List of ProjectListingDto toDomainProjects should map correctly`() {
        // Given
        val dtos = listOf(
            ProjectListingDto(1, "Project 1"),
            ProjectListingDto(2, "Project 2")
        )

        // When
        val result = dtos.toDomainProjects()

        // Then
        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Project 1", result[0].name)
        assertEquals(2, result[1].id)
        assertEquals("Project 2", result[1].name)
    }

    @Test
    fun `List of TaskInfoListing toDomainTasks should map correctly`() {
        // Given
        val dtos = listOf(
            TaskInfoListing(1, "Task 1", false, "user1", null),
            TaskInfoListing(2, "Task 2", true, "user2", null)
        )

        // When
        val result = dtos.toDomainTasks()

        // Then
        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Task 1", result[0].name)
        assertEquals(TaskStatus.TO_DO, result[0].status)
        assertEquals(2, result[1].id)
        assertEquals("Task 2", result[1].name)
        assertEquals(TaskStatus.DONE, result[1].status)
    }
}
