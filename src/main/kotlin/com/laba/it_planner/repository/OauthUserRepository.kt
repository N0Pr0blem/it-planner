package com.laba.it_planner.repository

import com.laba.it_planner.model.user.OauthUser
import org.springframework.data.jpa.repository.JpaRepository

interface OauthUserRepository: JpaRepository<OauthUser, Long> {
}