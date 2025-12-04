package com.example.planner.data.dto.task

import com.example.planner.data.model.repo.FileType

class TaskFileDto {
    var id: Long? = null
    var taskId: Long? = null
    var name: String? = null
    var type: FileType? = null
}