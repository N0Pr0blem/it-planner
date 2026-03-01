package com.laba.it_planner.repository.task

import com.laba.it_planner.model.task.Task
import com.laba.it_planner.repository.projection.TaskListingProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: JpaRepository<Task, Long> {
    @Query("""
        select t.id,
       t.name,
       t.is_completed as isCompleted,
       ui.first_name as firstName,
       ui.second_name as secondName,
       t.status as status,
       ui.profile_image as profileImage,
       ui.id as userId
from task t
         left join employee e on e.id = t.to_user_id
         left join user_info ui on e.user_id = ui.id
where t.project_id = :project_id
    """, nativeQuery = true)
    fun findAllByProjectId(@Param("project_id") projectId: Long): List<TaskListingProjection>

    @Query("""
    select t.id,
           t.name,
           t.is_completed as isCompleted,
           ui.first_name as firstName,
           ui.second_name as secondName,
           ui.profile_image as profileImage,
           ui.id as userId
    from task t
             left join employee e on e.id = t.to_user_id
             left join user_info ui on e.user_id = ui.id
    where ui.email=:username
        """, nativeQuery = true)
    fun findAllByUsername(@Param("username")username: String): List<TaskListingProjection>

    @Query("""
        Select t.* from task t
         where t.id=:task_id
    """, nativeQuery = true)
    fun getByTaskId(@Param("task_id") taskId: Long): Task
}