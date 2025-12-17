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
                DataException("error.user.username.not_found", username)
            }
    }

    override fun register(registerRequestDto: RegisterRequestDto): UserInfo {
        if (userInfoRepository.findByUsername(registerRequestDto.username).isPresent) {
            val username = registerRequestDto.username
            throw DataException("error.user.username.exists", username)
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
            throw AuthException("error.user.disabled", "")
        }
        if (user.password != securityService.hashPassword(oauthRequestDto.password)) {
            throw AuthException("error.user.password", "")
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
        } else throw AccessException("error.user.verification_code", "")

        return MessageResponseDto(message = result)
    }

    private fun generate4DigitCode(): String {
        return RandomStringUtils.random(4, false, true)
    }
}
