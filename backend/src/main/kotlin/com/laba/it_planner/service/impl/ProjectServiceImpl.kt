package com.laba.it_planner.service.impl

import com.laba.it_planner.dto.employee.EmployeeInviteDto
import com.laba.it_planner.dto.employee.EmployeeUpdateRoleDto
import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.project.repository.ProjectRepo
import com.laba.it_planner.model.user.ProjectRole
import com.laba.it_planner.repository.ProjectRepository
import com.laba.it_planner.service.*
import org.springframework.stereotype.Service
import java.security.Principal
import java.time.LocalDateTime

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val oauthService: OauthService,
    private val employeeService: EmployeeService,
    private val projectRepoService: ProjectRepoService,
    private val userInfoService: UserInfoService,
    private val mailService: MailService
) : ProjectService {

    override fun createProject(
        projectCreateRequestDto: ProjectCreateRequestDto,
        principal: Principal
    ): Project {
        val userInfo = userInfoService.getUserInfo(principal)
        if (projectRepository.findByNameAndByCreatedUser(projectCreateRequestDto.name, userInfo.id).isPresent) {
            throw DataException(
                "Project with name ${projectCreateRequestDto.name} already exists",
                "PROJECT_CREATION_ERROR"
            )
        } else {
            val result = projectRepository.save(
                Project(
                    name = projectCreateRequestDto.name,
                    creationDate = LocalDateTime.now(),
                    createdUser = userInfo
                )
            )

            val employee = Employee(
                user = userInfo,
                project = result,
                projectRole = ProjectRole.PROJECT_MANAGER
            )

            projectRepoService.createProjectRepo(
                ProjectRepo(
                    project = result,
                    path = result.createdUser?.username + "/" + result.name + "/"
                )
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
        } else throw DataException("Project with id $projectId does not exist", "PROJECT_NOT_FOUND_ERROR")
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
            throw DataException(
                "Project with id $projectId does not exist or it's not your's",
                "PROJECT_NOT_FOUND_ERROR"
            )
        }
    }

    override fun get(projectId: Long): Project {
        return projectRepository.findById(projectId)
            .orElseThrow { DataException("Project with id $projectId not found", "NOT_FOUND_ERROR") }
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