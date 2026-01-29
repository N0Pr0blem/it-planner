package com.laba.it_planner.mapper.user

import com.laba.it_planner.dto.userInfo.UserInfoResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.user.UserInfo
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserInfoMapper : Mappable<UserInfo, UserInfoResponseDto>{
}
