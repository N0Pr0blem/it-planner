package com.laba.it_planner.service.project.impl

import com.laba.it_planner.dto.employee.EmployeeInviteDto
import com.laba.it_planner.dto.employee.EmployeeUpdateRoleDto
import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.user.ProjectRole
import com.laba.it_planner.repository.project.ProjectRepository
import com.laba.it_planner.service.*
import com.laba.it_planner.service.mail.MailService
import com.laba.it_planner.service.project.EmployeeService
import com.laba.it_planner.service.project.ProjectService
import com.laba.it_planner.service.storage.StorageService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.security.Principal
import java.time.LocalDateTime
import java.util.Locale.getDefault
import java.util.UUID

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val oauthService: OauthService,
    private val employeeService: EmployeeService,
    private val storageService: StorageService,
    private val userInfoService: UserInfoService,
    private val mailService: MailService
) : ProjectService {

    @Transactional
    override fun createProject(
        projectCreateRequestDto: ProjectCreateRequestDto,
        principal: Principal
    ): Project {
        val userInfo = userInfoService.getUserInfo(principal)
        if (projectRepository.findByNameAndByCreatedUser(projectCreateRequestDto.name.lowercase(getDefault()), userInfo.id).isPresent) {
            throw DataException(
                "error.project.exist",
                projectCreateRequestDto.name
            )
        } else {
            val storage = storageService.createStorage(
                Storage(
                    path = ("users/user_${userInfo.id}/projects/${UUID.randomUUID()}/storage"),
                    files = emptyList(),
                )
            )

            val result = projectRepository.save(
                Project(
                    name = projectCreateRequestDto.name,
                    creationDate = LocalDateTime.now(),
                    createdUser = userInfo,
                    storage = storage
                )
            )

            val employee = Employee(
                user = userInfo,
                project = result,
                projectRole = ProjectRole.PROJECT_MANAGER
            )

            employeeService.createEmployee(employee)

            return result
        }
    }

    override fun inviteEmployee(
        projectId: Long,
        employeeInviteDto: EmployeeInviteDto,
        username: String
    ): Employee {
        val projects = getAllUsersProjects(username)
        val project = projectRepository.findById(projectId)
        if (project.isPresent && projects.contains(project.get())) {
            val user = userInfoService.getUserInfo(employeeInviteDto.username)
            mailService.sendInformationForm(user.email!!, "You have been invited to this project ${project.get().name}")
            return employeeService.createEmployee(
                Employee(
                    user = user,
                    project = project.get(),
                    projectRole = employeeInviteDto.projectRole
                )
            )
        } else throw DataException("error.project.not_exist", projectId.toString())
    }

    override fun deleteEmployee(projectId: Long, employeeId: Long, username: String) {
        val projects = getAllUsersProjects(username)
        val project = projectRepository.findById(projectId)
        val employee = employeeService.getById(employeeId)
        if (project.isPresent
            && projects.contains(project.get())
            && employee.user.username != username
            && project.get().createdUser!!.username != username
        ) {
            employeeService.deleteEmployee(employeeId)
        }
    }

    override fun changeRole(
        projectId: Long,
        employeeId: Long,
        employeeUpdateRoleDto: EmployeeUpdateRoleDto,
        name: String
    ): Employee {
        val project = projectRepository.findById(projectId)
        if (project.isPresent && project.get().createdUser?.username.equals(name)) {
            return employeeService.changeRole(employeeId, employeeUpdateRoleDto.projectRole)
        } else {
            throw DataException("error.project.not_exist", projectId.toString())
        }
    }

    override fun get(projectId: Long): Project {
        return projectRepository.findById(projectId)
            .orElseThrow { DataException("error.project.not_exist", projectId.toString()) }
    }

    override fun getAllProjects(name: String): List<Project> {
        return projectRepository.getAllUsersProjectsByUsername(name)
    }

    override fun deleteProject(projectId: Long, principal: Principal) {
        if (employeeService.checkPermission(projectId, principal)) {
            projectRepository.deleteById(projectId)
        }
    }

    override fun getByTaskId(taskId: Long): Project {
        return projectRepository.findByTaskId(taskId)
    }

    override fun getAllUsersProjects(username: String): List<Project> {
        val user = oauthService.getByUsername(username)
        return projectRepository.findAllByCreatedUser(user)
    }

}