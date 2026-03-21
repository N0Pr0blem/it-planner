package com.laba.it_planner.repository.task

import com.laba.it_planner.model.task.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByTaskId(id: Long?): List<Comment>
}