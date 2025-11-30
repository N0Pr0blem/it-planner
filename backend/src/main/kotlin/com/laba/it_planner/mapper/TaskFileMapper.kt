package com.laba.it_planner.mapper

import com.laba.it_planner.dto.task.TaskFileDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.task.TaskFile
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TaskFileMapper : Mappable<TaskFile,TaskFileDto>{
    @Mapping(target = "taskId", source = "taskInfo.id")
    override fun toDto(entity: TaskFile): TaskFileDto
}