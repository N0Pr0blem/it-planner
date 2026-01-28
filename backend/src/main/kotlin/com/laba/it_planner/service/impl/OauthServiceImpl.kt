package com.laba.it_planner.service.impl

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.exception.AccessException
import com.laba.it_planner.exception.AuthException
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.user.OauthRole
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.repository.OauthUserRepository
import com.laba.it_planner.repository.UserInfoRepository
import com.laba.it_planner.security.TokenDetails
import com.laba.it_planner.service.MailService
import com.laba.it_planner.service.OauthService
import com.laba.it_planner.service.SecurityService
import com.laba.it_planner.utils.feature.FeatureToggleService
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OauthServiceImpl(
    private val userInfoRepository: UserInfoRepository,
    private val oauthRepository: OauthUserRepository,
    private val securityService: SecurityService,
    private val mailService: MailService,
    private val toggleService: FeatureToggleService
) : OauthService {

    override fun getByUsername(username: String): OauthUser {
        return oauthRepository.findByUsername(username)
            .orElseThrow {
                DataException("User with username $username not found", "USER_NOT_FOUND")
            }
    }

    override fun register(registerRequestDto: RegisterRequestDto): UserInfo {
        if (userInfoRepository.findByUsername(registerRequestDto.username).isPresent) {
            val username = registerRequestDto.username
            throw DataException("User with username $username already exist", "USER_ALREADY_EXIST")
        }
        val generatedVerificationCode = generate4DigitCode();
        val userInfo = UserInfo().apply {
            email = registerRequestDto.username
            firstName = registerRequestDto.firstName
            secondName = ""
            lastName = ""

            username = registerRequestDto.username
            password = securityService.hashPassword(registerRequestDto.password)
            enabled = !toggleService.isEnabled("email.sending")
            verificationCode = generatedVerificationCode
            role = OauthRole.USER
            registrationDate = LocalDateTime.now()
        }
        mailService.sendActivationCodeForm(userInfo.email!!, generatedVerificationCode)
        return userInfoRepository.save(userInfo)
    }

    override fun authenticate(oauthRequestDto: AuthRequestDto): TokenDetails {
        val user = getByUsername(oauthRequestDto.username)
        if (!user.enabled) {
            throw AuthException("Account disabled", "ACCOUNT_DISABLED")
        }
        if (user.password != securityService.hashPassword(oauthRequestDto.password)) {
            throw AuthException("Account password mismatch", "INVALID_PASSWORD")
        }

        return securityService.generateToken(user)
    }

    override fun verify(username: String, code: String): MessageResponseDto {
        val user = getByUsername(username)
        val result = if (user.verificationCode == code) {
            user.verificationCode = null
            user.enabled = true
            oauthRepository.save(user)
            "$username successfully verified"
        } else throw AccessException("Wrong verification code", "VERIFICATION_CODE_EXCEPTION")

        return MessageResponseDto(message = result)
    }

    override fun resendVerificationCode(username: String): MessageResponseDto {
        val userInfo = userInfoRepository.findByUsername(username)
            .orElseThrow { DataException("User with username $username not found", "USER_NOT_FOUND") }
        if (userInfo.enabled) {
            throw AccessException("Account already verified", "ACCOUNT_ALREADY_VERIFIED")
        }
        val generatedVerificationCode = generate4DigitCode()
        userInfo.verificationCode = generatedVerificationCode
        userInfo.enabled = false
        userInfoRepository.save(userInfo)
        mailService.sendActivationCodeForm(userInfo.email!!, generatedVerificationCode)
        return MessageResponseDto(message = "Verification code resent")
    }

    private fun generate4DigitCode(): String {
        return RandomStringUtils.random(4, false, true)
    }
}
