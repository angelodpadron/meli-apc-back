package org.meliapp.backend.service

import org.meliapp.backend.model.Role
import org.meliapp.backend.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository
            .findByEmail(username)
            .orElseThrow { UsernameNotFoundException(username) }

        return User
            .builder()
            .username(user.email)
            .password(user.password)
            .authorities(mapRolesToAuthorities(user.roles))
            .build()

    }

    private fun mapRolesToAuthorities(roles: Collection<Role>): Collection<GrantedAuthority> {
        return roles
            .stream()
            .map { SimpleGrantedAuthority(it.name.toString()) }
            .collect(Collectors.toList())
    }
}