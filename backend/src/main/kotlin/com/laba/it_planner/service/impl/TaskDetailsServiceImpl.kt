package com.laba.it_planner.service.impl

import com.laba.it_planner.model.task.TaskDetails
import com.laba.it_planner.repository.TaskDetailsRepository
import com.laba.it_planner.service.OauthService
import com.laba.it_planner.service.TaskDetailsService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class TaskDetailsServiceImpl(
    private val taskDetailsRepository: TaskDetailsRepository,
    private val oauthService: OauthService
) : TaskDetailsService {
    override fun getEmpty(principal: Principal): TaskDetails {
        val user = oauthService.getByUsername(principal.name)
        return taskDetailsRepository.save(TaskDetails(fromUser = user))
    }

}