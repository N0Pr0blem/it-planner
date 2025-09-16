package com.laba.it_planner.service.impl

import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.security.Encoder
import com.laba.it_planner.security.TokenDetails
import com.laba.it_planner.service.SecurityService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class SecurityServiceImpl(
    private val encoder: Encoder,

    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.expiration}")
    private val expirationInSeconds: Integer,

    @Value("\${jwt.issuer}")
    private val issuer: String

) : SecurityService {

    override fun hashPassword(password: String): String {
        return encoder.encode(password)
    }

    override fun generateToken(user: OauthUser): TokenDetails {
        val claims = mapOf<String, Any>(
            "username" to user.username as Any,
            "role" to user.role as Any
        )

        return generateToken(claims, user.id.toString())
    }

    private fun generateToken(claims: Map<String, Any>, subject: String): TokenDetails {
        val expirationTimeInMillis: Long = (expirationInSeconds as Long) * 1000L
        val expirationDate = Date(System.currentTimeMillis() + expirationTimeInMillis)

        return generateToken(expirationDate, claims, subject)
    }

    private fun generateToken(expirationDate: Date, claims: Map<String, Any>, subject: String): TokenDetails {
        val createdDate = Date()
        val token = Jwts.builder()
            .setClaims(claims)
            .setIssuer(issuer)
            .setSubject(subject)
            .setIssuedAt(createdDate)
            .setId(UUID.randomUUID().toString())
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.toByteArray()))
            .compact()

        return TokenDetails(
            token = token,
            expiresAt = expirationDate,
            issuedAt = createdDate
        )
    }

    override fun validateToken(authHeader: String): Boolean {
        try {
            var token = authHeader.trim()
            if (token.startsWith("Bearer ")) {
                token = token.substring(7)
            }

            Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.toByteArray()))
                .build()
                .parseClaimsJws(token)


            return true
        } catch (e: ExpiredJwtException) {
            System.err.println("Token expired: " + e.message)
            return false
        } catch (e: Exception) {
            System.err.println("Token validation error: " + e.message)
            return false
        }
    }

    override fun getUsernameFromToken(token: String): String {
        return Jwts.parser()
            .setSigningKey(Base64.getEncoder().encodeToString(secret.toByteArray()))
            .build()
            .parseClaimsJws(token)
            .getBody()["username"].toString()
    }
}