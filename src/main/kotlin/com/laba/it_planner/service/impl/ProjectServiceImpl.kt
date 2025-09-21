package com.laba.it_planner.service.impl

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

            projectRepoService.createProjectRepo(ProjectRepo(
                project = result,
                path = result.createdUser?.username + "/" + result.name + "/")
            )

            employeeService.createEmployee(employee)

            return result
        }
    }
}