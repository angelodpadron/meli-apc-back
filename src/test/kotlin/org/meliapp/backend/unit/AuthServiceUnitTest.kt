package org.meliapp.backend.unit

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.meliapp.backend.config.security.filters.jwt.JWTHelper
import org.meliapp.backend.dto.auth.AuthRequestBody
import org.meliapp.backend.model.Role
import org.meliapp.backend.model.RoleName
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.RoleRepository
import org.meliapp.backend.repository.UserRepository
import org.meliapp.backend.service.AuthService
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever


@ExtendWith(MockitoExtension::class)
class AuthServiceUnitTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var roleRepository: RoleRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var jwtHelper: JWTHelper

    @InjectMocks
    private lateinit var authService: AuthService

    @Test
    fun `a user should have encoded password and default user role when created`() {
        // Arrange
        val request = AuthRequestBody("pepe@email.com", "password")
        val mockEncodedPassword = "encoded_password"
        val userRole = Role().apply {
            id = 1L
            name = RoleName.ROLE_USER
        }

        whenever(passwordEncoder.encode(any())).thenReturn(mockEncodedPassword)
        whenever(roleRepository.findRoleByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole))

        // Act
        val user = authService.generateUser(request)

        // Assert
        assertEquals(request.email, user.email)
        assertEquals(mockEncodedPassword, user.password)
        assertEquals(userRole, user.roles.first())
    }

    @Test
    fun `should throw exception when user with the same email already exists`() {
        // Arrange
        val email = "pepe@email.com"
        whenever(userRepository.existsByEmail(email)).thenReturn(true)

        // Act and Assert
        val exception = assertThrows<RuntimeException> {
            authService.checkIfUserExists(email)
        }

        assertEquals("User already registered: $email", exception.message)
    }

    @Test
    fun `should authenticate user and set security context`() {
        // Arrange
        val email = "pepe@email.com"
        val password = "password"
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
        val mockAuthentication = mock(Authentication::class.java)

        whenever(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(
            mockAuthentication
        )

        // Act
        authService.authenticate(AuthRequestBody(email, password))

        // Assert
        verify(authenticationManager).authenticate(authenticationToken)
        assertEquals(mockAuthentication, SecurityContextHolder.getContext().authentication)
    }


    @Test
    fun `should register a new user and generate JWT`() {
        // Arrange
        val request = AuthRequestBody("pepe@email.com", "password")
        val mockEncodedPassword = "encoded_password"
        val userRole = Role().apply {
            id = 1L
            name = RoleName.ROLE_USER
        }
        val mockToken = "jwt_token"

        whenever(passwordEncoder.encode(any())).thenReturn(mockEncodedPassword)
        whenever(roleRepository.findRoleByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole))
        whenever(jwtHelper.generateToken(request.email)).thenReturn(mockToken)

        // Act
        val token = authService.register(request)

        // Assert
        val captor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(captor.capture())

        val savedUser = captor.value
        assertEquals(request.email, savedUser.email)
        assertEquals(mockEncodedPassword, savedUser.password)
        assertEquals(userRole, savedUser.roles.first())
        assertEquals(mockToken, token)
    }


    @Test
    fun `should throw authentication exception when bad credentials test`() {
        // Arrange
        val request = AuthRequestBody("pepe@email.com", "password")

        whenever(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenThrow(
            BadCredentialsException("")
        )

        // Act and Assert
        assertThrows<AuthenticationException> { authService.login(request) }

    }

    @Test
    fun `should return a jwt when login`() {
        // Arrange
        val request = AuthRequestBody("pepe@email.com", "password")
        val mockUser = mock(User::class.java)

        whenever(userRepository.findByEmail(request.email)).thenReturn(Optional.of(mockUser))
        whenever(mockUser.email).thenReturn(request.email)
        whenever(jwtHelper.generateToken(request.email)).thenReturn("JWT")

        val result = authService.login(request)

        assertEquals("JWT", result)

    }

}