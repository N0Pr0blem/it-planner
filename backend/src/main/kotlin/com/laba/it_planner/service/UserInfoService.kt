package com.laba.it_planner.service

import com.laba.it_planner.dto.userInfo.UserInfoPatchDto
import com.laba.it_planner.model.user.UserInfo
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

interface UserInfoService {
    fun update(userInfoPatchDto: UserInfoPatchDto, multipartFile: MultipartFile?, principal: Principal): UserInfo
    fun getInfo(principal: Principal):UserInfo
}