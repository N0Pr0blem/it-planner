package com.laba.it_planner.repository

import com.laba.it_planner.model.user.UserInfo
import org.springframework.data.jpa.repository.JpaRepository

interface UserInfoRepository: JpaRepository<UserInfo, Long> {
}