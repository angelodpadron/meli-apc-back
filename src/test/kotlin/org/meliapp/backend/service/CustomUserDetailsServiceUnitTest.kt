package org.meliapp.backend.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.meliapp.backend.model.Role
import org.meliapp.backend.model.RoleName
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.UserRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import org.mockito.Mockito.`when` as whenever


@ExtendWith(MockitoExtension::class)
class CustomUserDetailsServiceUnitTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var customUserDetailsService: CustomUserDetailsService

    @Test
    fun `loadUserByUsername should return UserDetails when user is found`() {
        // Arrange
        val username = "test@example.com"
        val password = "password"
        val roles = listOf(Role().apply { name = RoleName.ROLE_USER })

        val user = mock(User::class.java)

        whenever(user.email).thenReturn(username)
        whenever(user.password).thenReturn(password)
        whenever(user.roles).thenReturn(roles)
        whenever(userRepository.findByEmail(username)).thenReturn(Optional.of(user))

        // Act
        val userDetails: UserDetails = customUserDetailsService.loadUserByUsername(username)

        // Assert
        assertEquals(username, userDetails.username)
        assertEquals(password, userDetails.password)
        assertTrue(userDetails.authorities.contains(SimpleGrantedAuthority("ROLE_USER")))
    }

    @Test
    fun `loadUserByUsername should throw UsernameNotFoundException when user is not found`() {
        // Arrange
        val username = "test@example.com"

        whenever(userRepository.findByEmail(username)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<UsernameNotFoundException> {
            customUserDetailsService.loadUserByUsername(username)
        }
    }

}