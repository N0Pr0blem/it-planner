package com.laba.it_planner.repository.user

import com.laba.it_planner.model.user.OauthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface OauthUserRepository: JpaRepository<OauthUser, Long> {
    fun findByUsername(username: String?): Optional<OauthUser>
}