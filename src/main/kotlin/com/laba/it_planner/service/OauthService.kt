package com.laba.it_planner.service

import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.security.TokenDetails

interface OauthService {
    fun getByUsername(username: String): OauthUser

    fun register(registerRequestDto: RegisterRequestDto): OauthUser

    fun authenticate(oauthRequestDto: AuthRequestDto): TokenDetails

    //fun activate(username: String, password: String): OauthUser
}