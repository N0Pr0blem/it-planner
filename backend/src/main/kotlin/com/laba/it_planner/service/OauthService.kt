package com.laba.it_planner.service

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.security.TokenDetails

interface OauthService {
    fun getByUsername(username: String): OauthUser

    fun register(registerRequestDto: RegisterRequestDto): UserInfo

    fun authenticate(oauthRequestDto: AuthRequestDto): TokenDetails

    fun verify(username: String, code: String): MessageResponseDto

    fun resendVerificationCode(username: String): MessageResponseDto
}
