package com.laba.it_planner.repository.task

import com.laba.it_planner.model.task.Tracking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TrackingRepository : JpaRepository<Tracking, Long> {
    @Query(
        """
        SELECT tt.* FROM task_tracking tt
                    LEFT JOIN task t ON t.id = tt.task_id
        WHERE t.id = :task_id
    """, nativeQuery = true
    )
    fun findAllByTaskId(@Param("task_id") taskId: Long):List<Tracking>
}