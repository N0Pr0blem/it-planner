package com.laba.it_planner.mapper

import com.laba.it_planner.dto.task.TaskInfoResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.task.TaskInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TaskInfoMapper: Mappable<TaskInfo, TaskInfoResponseDto> {
    @Mapping(target = "projectId", source = "project.id")
    override fun toDto(entity: TaskInfo): TaskInfoResponseDto
}