package com.laba.it_planner.repository

import com.laba.it_planner.model.user.OauthUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface OauthUserRepository: JpaRepository<OauthUser, Long> {
    fun findByUsername(username: String?): Optional<OauthUser>
}