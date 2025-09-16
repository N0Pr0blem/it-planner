package com.laba.it_planner.service.impl

import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.exception.AuthException
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.user.OauthRole
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.repository.OauthUserRepository
import com.laba.it_planner.repository.UserInfoRepository
import com.laba.it_planner.security.TokenDetails
import com.laba.it_planner.service.OauthService
import com.laba.it_planner.service.SecurityService
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service

@Service
class OauthServiceImpl(
    private val userInfoRepository: UserInfoRepository,
    private val oauthRepository: OauthUserRepository,
    private val securityService: SecurityService
) : OauthService {

    override fun getByUsername(username: String): OauthUser {
        return oauthRepository.findByUsername(username)
            .orElseThrow {
                DataException("User with username $username not found", "USER_NOT_FOUND")
            }
    }

    override fun register(registerRequestDto: RegisterRequestDto): UserInfo {
        if(userInfoRepository.findByUsername(registerRequestDto.username).isPresent){
            val username = registerRequestDto.username
            throw DataException("User with username $username already exist", "USER_ALREADY_EXIST")
        }
        val userInfo = UserInfo().apply{
            email = registerRequestDto.username
            firstName = registerRequestDto.firstName

            username = registerRequestDto.username
            password = securityService.hashPassword(registerRequestDto.password)
            enabled = true
            verificationCode = generate6DigitCode()
            role = OauthRole.USER
        }
        return userInfoRepository.save(userInfo)
    }

    override fun authenticate(oauthRequestDto: AuthRequestDto): TokenDetails {
        val user = getByUsername(oauthRequestDto.username)
        if(!user.enabled) {
            throw AuthException("Account disabled", "ACCOUNT_DISABLED")
        }
        if(user.password != securityService.hashPassword(oauthRequestDto.password)){
            throw AuthException("Account password mismatch", "INVALID_PASSWORD")
        }

        return securityService.generateToken(user)
    }

    private fun generate6DigitCode(): String {
        return RandomStringUtils.random(6, false, true)
    }
}
