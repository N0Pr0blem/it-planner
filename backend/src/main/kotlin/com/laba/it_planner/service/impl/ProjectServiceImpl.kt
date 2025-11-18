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
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.OauthService
import com.laba.it_planner.service.ProjectRepoService
import com.laba.it_planner.service.ProjectService
import org.springframework.stereotype.Service
import java.security.Principal
import java.time.LocalDateTime

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val oauthService: OauthService,
    private val employeeService: EmployeeService,
    private val projectRepoService: ProjectRepoService
) : ProjectService {

    override fun createProject(
        projectCreateRequestDto: ProjectCreateRequestDto,
        username: String
    ): Project {
        val user = oauthService.getByUsername(username)
        if (projectRepository.findByNameAndByCreatedUser(projectCreateRequestDto.name, user.id).isPresent) {
            throw DataException(
                "Project with name ${projectCreateRequestDto.name} already exists",
                "PROJECT_CREATION_ERROR"
            )
        } else {
            val result = projectRepository.save(
                Project(
                    name = projectCreateRequestDto.name,
                    creationDate = LocalDateTime.now(),
                    createdUser = user
                )
            )

            val employee = Employee(
                user = user,
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
            val user = oauthService.getByUsername(employeeInviteDto.username)
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
        if (project.isPresent && projects.contains(project.get())) {
            employeeService.deleteEmployee(employeeId)
        }
    }

    override fun changeRole(
        projectId: Long,
        employeeId: Long,
        employeeUpdateRoleDto: EmployeeUpdateRoleDto,
        name: String
    ) : Employee{
        val project = projectRepository.findById(projectId)
        if(project.isPresent && project.get().createdUser?.username.equals(name)) {
           return employeeService.changeRole(employeeId,employeeUpdateRoleDto.projectRole)
        }
        else{
            throw DataException("Project with id $projectId does not exist or it's not your's", "PROJECT_NOT_FOUND_ERROR")
        }
    }

    override fun get(projectId: Long): Project {
        return projectRepository.findById(projectId)
            .orElseThrow { DataException("Project with id $projectId not found","NOT_FOUND_ERROR") }
    }

    override fun getAllUsersProjects(username: String): List<Project> {
        val user = oauthService.getByUsername(username)
        return projectRepository.findAllByCreatedUser(user)
    }

}