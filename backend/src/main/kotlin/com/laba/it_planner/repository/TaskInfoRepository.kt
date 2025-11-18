package com.laba.it_planner.repository

import com.laba.it_planner.model.task.TaskInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskInfoRepository: JpaRepository<TaskInfo, Long> {
    fun findAllByProjectId(projectId: Long): List<TaskInfo>
}