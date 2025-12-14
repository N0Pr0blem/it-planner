package com.laba.it_planner.mapper

import com.laba.it_planner.dto.userInfo.UserInfoForTaskDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.user.UserInfo
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMainInfoMapper: Mappable<UserInfo, UserInfoForTaskDto> {
}