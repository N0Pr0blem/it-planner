package com.laba.it_planner.repository

import com.laba.it_planner.model.task.TaskDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskDetailsRepository: JpaRepository<TaskDetails, Long> {
}