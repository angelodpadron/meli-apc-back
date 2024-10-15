package org.meliapp.backend.service

import org.meliapp.backend.config.security.filters.jwt.JWTHelper
import org.meliapp.backend.dto.auth.AuthRequestBody
import org.meliapp.backend.exception.apc.UserAlreadyRegisteredException
import org.meliapp.backend.model.RoleName
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.RoleRepository
import org.meliapp.backend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtHelper: JWTHelper
) {
    fun register(registerRequest: AuthRequestBody): String {
        checkIfUserExists(registerRequest.email)

        val user = generateUser(registerRequest)

        userRepository.save(user)

        authenticate(registerRequest)

        return jwtHelper.generateToken(user.email)
    }

    fun generateUser(registerRequest: AuthRequestBody): User {
        return User().apply {
            email = registerRequest.email
            password = passwordEncoder.encode(registerRequest.password)
            roles = setOf(
                roleRepository
                    .findRoleByName(RoleName.ROLE_USER)
                    .orElseThrow { RuntimeException("Role not found: ${RoleName.ROLE_USER.name}") }
            )
        }
    }

    fun login(request: AuthRequestBody): String {
        authenticate(request)

        return userRepository
            .findByEmail(request.email)
            .map { user -> jwtHelper.generateToken(user.email) }
            .get()
    }

    fun authenticate(request: AuthRequestBody) {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
    }

    fun checkIfUserExists(email: String) {
        if (userRepository.existsByEmail(email)) throw UserAlreadyRegisteredException(email)
    }

    fun getUserAuthenticated(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        return userRepository.findByEmail(email).orElseThrow { RuntimeException("Cannot retrieve authenticated user with email: $email") }
    }

}