package com.laba.it_planner.dto.task

import com.laba.it_planner.model.project.repository.FileType

class TaskFileDto {
    var id: Long? = null
    var taskId: Long? = null
    var name: String? = null
    var type: FileType? = null
}