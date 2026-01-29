package com.laba.it_planner.repository.user

import com.laba.it_planner.model.user.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserInfoRepository: JpaRepository<UserInfo, Long> {
    fun findByUsername(username: String): Optional<UserInfo>
}