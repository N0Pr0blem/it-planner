package com.laba.it_planner.config

import com.laba.it_planner.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
class SecurityConfig(
        private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    private val publicRoutes = arrayOf(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/activate",
            "/api/v1/swagger-ui/*",
            "/api/v1/swagger-ui.html",
            "/webjars/swagger-ui/**",
            "/v3/api-docs/**",
            "OPTIONS /**"
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf{it.disable()}
                .cors { cors ->
                cors.configurationSource { request ->
                CorsConfiguration().apply {
            allowedOrigins = listOf(
                    "http://localhost:5173",
                    "http://frontend:80",
                    "capacitor://localhost",
                    "ionic://localhost",
                    "http://localhost",
                    "http://10.0.2.2:8080"
            )
            allowedMethods = listOf("*")
            allowedHeaders = listOf("*")
            allowCredentials = true
            exposedHeaders = listOf("Authorization")
            maxAge = 3600L
        }
        }
        }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(*publicRoutes).permitAll()
                .requestMatchers("/api/v1/users/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/profile/**").authenticated()
                .anyRequest().authenticated()
        }

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}