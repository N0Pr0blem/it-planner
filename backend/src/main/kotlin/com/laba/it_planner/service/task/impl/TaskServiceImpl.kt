package com.laba.it_planner.service.task.impl

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.CreateTaskInfoRequestDto
import com.laba.it_planner.dto.task.TaskInfoListing
import com.laba.it_planner.dto.task.UpdateTaskInfoRequestDto
import com.laba.it_planner.exception.AccessException
import com.laba.it_planner.mapper.task.TaskDescriptionMapper
import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.task.Task
import com.laba.it_planner.model.task.enum_old.TaskStatus
import com.laba.it_planner.repository.projection.TaskListingProjection
import com.laba.it_planner.repository.task.TaskRepository
import com.laba.it_planner.service.mail.MailService
import com.laba.it_planner.service.project.EmployeeService
import com.laba.it_planner.service.project.ProjectService
import com.laba.it_planner.service.storage.FileService
import com.laba.it_planner.service.storage.StorageService
import com.laba.it_planner.service.task.TaskService
import jakarta.transaction.Transactional
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.Principal
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val employeeService: EmployeeService,
    private val projectService: ProjectService,
    private val fileService: FileService,
    private val taskDescriptionMapper: TaskDescriptionMapper,
    private val mailService: MailService,
    private val storageService: StorageService,
    private val messageSource: MessageSource,
) : TaskService {
    override fun get(id: Long, principal: Principal): Task {
        val taskInfoOpt = taskRepository.findById(id)
        if (taskInfoOpt.isPresent) {
            val taskInfo = taskInfoOpt.get()
            if (employeeService.checkPermission(taskInfo.project.id!!, principal)) {
                return taskInfo
            } else {
                throw AccessException("error.access.denied", "")
            }
        } else {
            throw AccessException("error.task.not_found", "")
        }
    }

    override fun getAll(
        projectId: Long,
        principal: Principal
    ): List<TaskInfoListing> {
        if (employeeService.checkPermission(projectId, principal)) {
            val dbResponse = taskRepository.findAllByProjectId(projectId)
            dbResponse.forEach { taskInfo -> println("${taskInfo.getId()} ${taskInfo.getName()} ${taskInfo.getFirstName()} ${taskInfo.getProfileImage()}") }
            return dbResponse.stream().map { projection -> fromProjection(projection) }
                .collect(Collectors.toList())
        } else {
            throw AccessException("error.access.denied", "")
        }
    }

    @Transactional
    override fun add(createTaskInfoRequestDto: CreateTaskInfoRequestDto, principal: Principal): Task {
        if (employeeService.checkPermission(createTaskInfoRequestDto.projectId, principal)) {
            val project = projectService.get(createTaskInfoRequestDto.projectId)
            val employee = employeeService.getByUserNameAndProjectId(principal.name, project.id!!)
            val taskUUID = UUID.randomUUID().toString();
            val path = fromProjectStorage(taskUUID, project.storage!!)
            return taskRepository.save(
                Task(
                    name = createTaskInfoRequestDto.name,
                    urgency = createTaskInfoRequestDto.urgency,
                    complexity = createTaskInfoRequestDto.complexity,
                    project = projectService.get(createTaskInfoRequestDto.projectId),
                    creationDate = LocalDateTime.now(),
                    status = TaskStatus.TO_DO,
                    isCompleted = false,
                    storage = storageService.createStorage(
                        Storage(
                            path = path,
                            files = emptyList()
                        )
                    ),
                    fromUser = employee,
                    toUser = null,
                    descriptionFile = fileService.createDescriptionFileForTask(
                        path,
                        principal,
                        taskUUID,
                        createTaskInfoRequestDto.description
                    )
                )
            )

        } else {
            throw AccessException("error.access.denied", "")
        }
    }

    private fun fromProjectStorage(taskName: String, storage: Storage): String {
        return storage.path.replace("/storage", "/tasks/${taskName}/storage")
    }

    override fun update(
        taskId: Long,
        updateTaskInfoRequestDto: UpdateTaskInfoRequestDto,
        principal: Principal
    ): Task {
        val task = get(taskId, principal)
        if (updateTaskInfoRequestDto.name != null) task.name = updateTaskInfoRequestDto.name!!
        if (updateTaskInfoRequestDto.complexity != null) task.complexity = updateTaskInfoRequestDto.complexity!!
        if (updateTaskInfoRequestDto.urgency != null) task.urgency = updateTaskInfoRequestDto.urgency!!
        if (updateTaskInfoRequestDto.status != null) task.status = updateTaskInfoRequestDto.status!!
        if (updateTaskInfoRequestDto.description != null) {
            fileService.updateFile(
                "${task.storage!!.path}/${task.descriptionFile!!}",
                updateTaskInfoRequestDto.description
            )
        }

        return taskRepository.save(task)
    }

    override fun delete(id: Long, principal: Principal) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id)
        }
    }

    override fun assignToMe(taskId: Long, projectId: Long, principal: Principal, locale: Locale): String {
        val taskOpt = taskRepository.findById(taskId)
        val employee = employeeService.getByUserNameAndProjectId(principal.name, projectId)
        if (taskOpt.isPresent) {
            val task = taskOpt.get()
            task.toUser = employee
            save(task)
            return messageSource.getMessage("message.task.assign.successful", arrayOf(""), locale)
        }

        return messageSource.getMessage("message.task.assign.exception", arrayOf(""), locale)
    }

    override fun getDescription(taskId: Long, principal: Principal): MessageResponseDto {
        val task = get(taskId, principal)
        if (task.descriptionFile != null) {
            val message = taskDescriptionMapper.toDto("${task.storage!!.path}/${task.descriptionFile!!}")
            return MessageResponseDto(message = message)
        } else return MessageResponseDto("")
    }

    override fun getMy(principal: Principal): List<TaskInfoListing>? {
        val dbResponse = taskRepository.findAllByUsername(principal.name)
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
                val image = fileService.getFile("users/user_${projection.getUserId()}/profile/${projection.getProfileImage()!!}")
                val encoded: ByteArray = Base64.getEncoder().encode(image)
                String(encoded, StandardCharsets.UTF_8)
            } else "null",
            status = projection.getStatus(),
        )
    }

    override fun assignToEmployee(
        taskId: Long,
        projectId: Long,
        employeeId: Long,
        principal: Principal
    ) {
        val taskOpt = taskRepository.findById(taskId)
        if (taskOpt.isPresent) {
            val task = taskOpt.get()
            val employee = employeeService.getById(employeeId)
            task.toUser = employee
            mailService.sendInformationForm(
                employee.user.email!!,
                "You was assigned to the task:\n${task.name}"
            )
            save(task)
        }
    }


    override fun getByTaskId(taskId: Long): Task {
        return taskRepository.getByTaskId(taskId)
    }

    override fun save(task: Task): Task {
        return taskRepository.save(task)
    }
}