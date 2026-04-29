package com.laba.it_planner.service.user.impl

import com.laba.it_planner.controller.user.OauthController
import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.exception.AccessException
import com.laba.it_planner.exception.AuthException
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.user.OauthRole
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.repository.user.OauthUserRepository
import com.laba.it_planner.repository.user.UserInfoRepository
import com.laba.it_planner.security.TokenDetails
import com.laba.it_planner.service.mail.MailService
import com.laba.it_planner.service.user.OauthService
import com.laba.it_planner.service.SecurityService
import com.laba.it_planner.utils.feature.FeatureToggleService
import jakarta.transaction.Transactional
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.logging.Logger

@Service
class OauthServiceImpl(
    private val userInfoRepository: UserInfoRepository,
    private val oauthRepository: OauthUserRepository,
    private val securityService: SecurityService,
    private val mailService: MailService,
    private val toggleService: FeatureToggleService
) : OauthService {
    private val logger: Logger = Logger.getLogger(OauthServiceImpl::class.java.name)

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
            logger.info("$username successfully verified by code $code")

            "$username successfully verified"
        } else throw AccessException("error.user.verification_code", "")

        return MessageResponseDto(message = result)
    }

    @Transactional
    override fun recoverCode(username: String): MessageResponseDto {
        val user = getByUsername(username)
        if(!user.enabled) {
            throw AuthException("error.user.disabled", "")
        }
        user.verificationCode = generate4DigitCode()
        mailService.sendActivationCodeForm(user.username!!, user.verificationCode!!)
        logger.info("Successfully send recover code for user ${user.username}")

        return MessageResponseDto(message = "Successfully send recover code for user ${user.username}")
    }

    @Transactional
    override fun recoverPassword(username: String, code: String, password: String)  : MessageResponseDto {
        val user = getByUsername(username)
        val result = if (user.verificationCode == code) {
            user.verificationCode = null
            user.password = securityService.hashPassword(password)
            oauthRepository.save(user)
            logger.info("$username successfully change password")
            mailService.sendInformationForm(user.username!!, "You successfully change password")

            "$username successfully change password"
        } else throw AccessException("error.user.verification_code", "")

        return MessageResponseDto(message = result)
    }

    private fun generate4DigitCode(): String {
        return RandomStringUtils.random(4, false, true)
    }
}
