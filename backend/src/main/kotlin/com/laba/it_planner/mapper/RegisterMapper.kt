package com.laba.it_planner.mapper

import com.laba.it_planner.dto.oauth.RegisterResponseDto
import com.laba.it_planner.dto.task.TaskInfoResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.task.TaskInfo
import com.laba.it_planner.model.user.UserInfo
import org.mapstruct.Mapper
import org.mapstruct.Mapping


@Mapper(componentModel = "spring")
interface RegisterMapper: Mappable<UserInfo, RegisterResponseDto> {
}