package org.meliapp.backend.config.security

import org.meliapp.backend.config.security.filters.jwt.JWTAuthFilter
import org.meliapp.backend.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@EnableWebSecurity
@Configuration

class SecurityConfiguration(
    private val jwtAuthFilter: JWTAuthFilter,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        return RoleHierarchyImpl
            .withDefaultRolePrefix()
            .role("ADMIN").implies("USER")
            .build()
    }

    @Bean
    fun configureFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {

        val userEndpoints = arrayOf(
            "api/purchases/**",
            "api/bookmarks/**"
        )

        val adminEndpoints = arrayOf(
            "api/admin/**"
        )

        return http
            .cors { cors ->
                cors.configurationSource {
                    val config = CorsConfiguration()
                    config.allowedOrigins = listOf("*")
                    config.allowedMethods = listOf("*")
                    config.allowedHeaders = listOf("*")
                    config
                }

            }
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/actuator/**"
                    ).permitAll()
                    .requestMatchers(HttpMethod.POST, "api/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "api/products/**").permitAll()
                    .requestMatchers(*userEndpoints).hasRole("USER")
                    .requestMatchers(*adminEndpoints).hasRole("ADMIN")

            }
            .authenticationManager(authenticationManager)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    }

    @Bean
    fun authenticationManager(
        http: HttpSecurity,
        customUserDetailsService: CustomUserDetailsService
    ): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)

        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder())

        return authenticationManagerBuilder.build()
    }

}