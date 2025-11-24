package com.laba.it_planner.repository

import com.laba.it_planner.model.task.TaskInfo
import com.laba.it_planner.repository.projection.TaskListingProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TaskInfoRepository: JpaRepository<TaskInfo, Long> {
    @Query("""
        select ti.id,
       ti.name,
       ti.is_completed as isCompleted,
       ui.first_name as firstName,
       ui.second_name as secondName,
       ui.profile_image as profileImage
from task_info ti
         left join task_details td on td.id = ti.task_details_id
         left join employee e on e.id = td.to_user_id
         left join user_info ui on e.user_id = ui.id
where ti.project_id = :project_id
    """, nativeQuery = true)
    fun findAllByProjectId(@Param("project_id") projectId: Long): List<TaskListingProjection>
}