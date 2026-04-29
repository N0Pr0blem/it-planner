package com.laba.it_planner.security;

import com.laba.it_planner.exception.AuthException;
import com.laba.it_planner.model.user.OauthUser;
import com.laba.it_planner.service.user.OauthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
class AuthenticationManagerImpl(
    private val oauthService: OauthService
) : AuthenticationManager {

    override fun authenticate(authentication: Authentication): Authentication {
        val principal = authentication.principal as CustomPrincipal
        val oauthUser: OauthUser = oauthService.getByUsername(principal.name)

        if (!oauthUser.enabled) {
            throw AuthException("This user hasn't been activated", "INACTIVE_USER")
        }

        return authentication
    }
}
