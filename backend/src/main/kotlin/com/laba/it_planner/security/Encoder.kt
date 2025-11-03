package com.laba.it_planner.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


@Component
class Encoder: PasswordEncoder {

    @Value("\${jwt.password.encoder.secret}")
    private val secret: String? = null

    @Value("\${jwt.password.encoder.iteration}")
    private val iteration: Int = 64

    @Value("\${jwt.password.encoder.key-length}")
    private val keyLength: Int = 256

    private val SECRET_KEY_INSTANCE: String = "PBKDF2WithHmacSHA512"

    override fun encode(rawPassword: CharSequence): String {
        try {
            val result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                .generateSecret(
                    PBEKeySpec(
                        rawPassword.toString().toCharArray(),
                        secret?.toByteArray(), iteration, keyLength
                    )
                )
                .getEncoded()
            return Base64.getEncoder().encodeToString(result)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            throw RuntimeException(e)
        }
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String?): Boolean {
        return encode(rawPassword) == encodedPassword
    }
}