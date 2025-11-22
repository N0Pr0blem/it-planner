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
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
        private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    private val publicRoutes = arrayOf(
            "/api/v1/auth/**",
            "/api/v1/auth/activate",
            "/api/v1/swagger-ui/*",
            "/api/v1/swagger-ui.html",
            "/webjars/swagger-ui/**",
            "/v3/api-docs/**",
            "OPTIONS /**"
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(*publicRoutes).permitAll()
                    .requestMatchers("/api/v1/users/**").hasAuthority("ADMIN")
                    .requestMatchers("/api/v1/profile/**").authenticated()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(
            "http://localhost:5173",
            "http://localhost:3000",
            "http://frontend:80",
            "http://localhost:8081", // Flutter web dev server
            "http://localhost:5000", // Альтернативный порт Flutter
            "http://127.0.0.1:8081", // localhost как IP
            "capacitor://localhost",
            "ionic://localhost",
            "http://localhost:8080",
            "http://10.0.2.2:8080"
        )
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        configuration.exposedHeaders = listOf("Authorization", "Content-Type")
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
