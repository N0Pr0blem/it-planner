package com.laba.it_planner.service

import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.security.TokenDetails

interface SecurityService {
    fun hashPassword(password: String): String

    fun generateToken(user: OauthUser): TokenDetails

    fun validateToken(authHeader:String): Boolean

    fun getUsernameFromToken(token: String): String
}