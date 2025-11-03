package com.laba.it_planner.security

import com.laba.it_planner.exception.ApiException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.Base64
import java.util.Date

class JwtHandler(private val secret: String) {

    fun check(accessToken: String): VerificationResult {
        return verify(accessToken)
    }

    private fun verify(token: String): VerificationResult {
        val claims = getClaimsFromToken(token)
        val expirationDate = claims.expiration

        if (expirationDate.before(Date())) {
            throw ApiException("Token expired", "TOKEN_EXCEPTION")
        }

        return VerificationResult(claims, token)
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body
    }

    class VerificationResult(
            val claims: Claims,
            val token: String
    )
}