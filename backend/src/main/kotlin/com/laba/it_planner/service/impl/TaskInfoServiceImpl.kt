package com.laba.it_planner.service.impl

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.CreateTaskInfoRequestDto
import com.laba.it_planner.dto.task.TaskInfoListing
import com.laba.it_planner.dto.task.UpdateTaskInfoRequestDto
import com.laba.it_planner.exception.AccessException
import com.laba.it_planner.mapper.TaskDescriptionMapper
import com.laba.it_planner.model.task.TaskInfo
import com.laba.it_planner.model.task.TaskStatus
import com.laba.it_planner.repository.TaskInfoRepository
import com.laba.it_planner.repository.projection.TaskListingProjection
import com.laba.it_planner.service.*
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.Principal
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@Service
class TaskInfoServiceImpl(
    private val taskInfoRepository: TaskInfoRepository,
    private val employeeService: EmployeeService,
    private val projectService: ProjectService,
    private val taskDetailService: TaskDetailsService,
    private val fileService: FileService,
    private val taskDescriptionMapper: TaskDescriptionMapper
) : TaskInfoService {
    override fun get(id: Long, principal: Principal): TaskInfo {
        val taskInfoOpt = taskInfoRepository.findById(id)
        if (taskInfoOpt.isPresent) {
            val taskInfo = taskInfoOpt.get()
            if (employeeService.checkPermission(taskInfo.project.id!!, principal)) {
                return taskInfo
            } else {
                throw AccessException("Access denied", "FORBIDDEN")
            }
        } else {
            throw AccessException("Task not found", "NOT_FOUND")
        }
    }

    override fun getAll(
        projectId: Long,
        principal: Principal
    ): List<TaskInfoListing> {
        if (employeeService.checkPermission(projectId, principal)) {
            val dbResponse = taskInfoRepository.findAllByProjectId(projectId)
            dbResponse.forEach { taskInfo -> println("${taskInfo.getId()} ${taskInfo.getName()} ${taskInfo.getFirstName()} ${taskInfo.getProfileImage()}") }
            return dbResponse.stream().map { projection -> fromProjection(projection) }
                .collect(Collectors.toList())
        } else {
            throw AccessException("Access denied", "FORBIDDEN")
        }
    }

    override fun add(createTaskInfoRequestDto: CreateTaskInfoRequestDto, principal: Principal): TaskInfo {
        if (employeeService.checkPermission(createTaskInfoRequestDto.projectId, principal)) {
            val project = projectService.get(createTaskInfoRequestDto.projectId)
            return taskInfoRepository.save(
                TaskInfo(
                    name = createTaskInfoRequestDto.name,
                    urgency = createTaskInfoRequestDto.urgency,
                    complexity = createTaskInfoRequestDto.complexity,
                    project = projectService.get(createTaskInfoRequestDto.projectId),
                    creationDate = LocalDateTime.now(),
                    status = TaskStatus.TO_DO,
                    taskDetails = taskDetailService.getEmpty(
                        project,
                        principal,
                        createTaskInfoRequestDto.description
                    )
                )
            )

        } else {
            throw AccessException("Access denied", "FORBIDDEN")
        }
    }

    override fun update(
        taskId: Long,
        updateTaskInfoRequestDto: UpdateTaskInfoRequestDto,
        principal: Principal
    ): TaskInfo {
        val task = get(taskId, principal)
        if (updateTaskInfoRequestDto.name != null) task.name = updateTaskInfoRequestDto.name!!
        if (updateTaskInfoRequestDto.complexity != null) task.complexity = updateTaskInfoRequestDto.complexity!!
        if (updateTaskInfoRequestDto.urgency != null) task.urgency = updateTaskInfoRequestDto.urgency!!
        if (updateTaskInfoRequestDto.status != null) task.status = updateTaskInfoRequestDto.status!!
        if (updateTaskInfoRequestDto.description != null) {
            fileService.updateFile(
                toDescriptionPath(taskId, task.taskDetails!!.descriptionFile!!),
                updateTaskInfoRequestDto.description
            )
        }

        return taskInfoRepository.save(task)
    }

    override fun delete(id: Long, principal: Principal) {
        if (taskInfoRepository.existsById(id)) {
            taskInfoRepository.deleteById(id)
        }
    }

    override fun assignToMe(taskId: Long, projectId: Long, principal: Principal) {
        val taskInfoOpt = taskInfoRepository.findById(taskId)
        val employee = employeeService.getByUserNameAndProjectId(principal.name, projectId)
        if (taskInfoOpt.isPresent) {
            val taskDetails = taskInfoOpt.get().taskDetails
            taskDetails!!.toUser = employee
            taskDetailService.save(taskDetails)
        }
    }

    override fun getDescription(taskId: Long, principal: Principal): MessageResponseDto {
        val taskDetails = taskDetailService.getByTaskId(taskId)
        if (taskDetails != null && taskDetails.descriptionFile != null) {
            val message = taskDescriptionMapper.toDto(toDescriptionPath(taskId, taskDetails.descriptionFile!!))
            return MessageResponseDto(message = message)
        } else return MessageResponseDto("")
    }

    override fun getMy(principal: Principal): List<TaskInfoListing>? {
        val dbResponse = taskInfoRepository.findAllByUsername(principal.name)
        dbResponse.forEach { taskInfo -> println("${taskInfo.getId()} ${taskInfo.getName()} ${taskInfo.getFirstName()} ${taskInfo.getProfileImage()}") }
        return dbResponse.stream().map { projection -> fromProjection(projection) }
            .collect(Collectors.toList())
    }

    private fun fromProjection(projection: TaskListingProjection): TaskInfoListing {
        return TaskInfoListing(
            id = projection.getId(),
            name = projection.getName(),
            isCompleted = projection.getIsCompleted() ?: false,
            assignBy = "${projection.getFirstName()} ${projection.getSecondName()}",
            assignByImage = if (projection.getProfileImage() != null) {
                val image = fileService.getFile(projection.getProfileImage()!!)
                val encoded: ByteArray = Base64.getEncoder().encode(image)
                String(encoded, StandardCharsets.UTF_8)
            } else "null",
            status = projection.getStatus(),
        )
    }

    fun toDescriptionPath(taskId: Long, taskFolder: String): String {
        return getTaskFolderPath(taskId, taskFolder) + "/description.txt"
    }

    fun getTaskFolderPath(taskId: Long, taskFolder: String): String {
        val project = projectService.getByTaskId(taskId)
        return "projects/${project.name}/${taskFolder}"
    }

    override fun getPathForTaskFolder(taskId: Long): String? {
        val taskInfoOpt = taskInfoRepository.findById(taskId)
        if(taskInfoOpt.isPresent){
            val taskDetails = taskInfoOpt.get().taskDetails
            return getTaskFolderPath(taskId, taskDetails!!.descriptionFile!!) + "/files/"
        }
        return null
    }
}