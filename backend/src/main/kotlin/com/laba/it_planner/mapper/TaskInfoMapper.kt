package com.laba.it_planner.mapper

import com.laba.it_planner.dto.task.TaskInfoResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.task.TaskInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring",uses = [UserInfoForTaskListingMapper::class])
interface TaskInfoMapper: Mappable<TaskInfo, TaskInfoResponseDto> {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "assignedBy", source = "taskDetails.fromUser.user")
    @Mapping(target = "assignedTo", source = "taskDetails.toUser.user")
    @Mapping(target = "urgency", source = "urgency.title")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "complexity", source = "complexity.title")
    override fun toDto(entity: TaskInfo): TaskInfoResponseDto
}