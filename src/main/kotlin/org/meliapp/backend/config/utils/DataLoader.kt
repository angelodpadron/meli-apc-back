package org.meliapp.backend.config.utils

import org.meliapp.backend.model.Role
import org.meliapp.backend.model.RoleName
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.RoleRepository
import org.meliapp.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class DataLoader(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    @Value("\${app.apc.admin.email}")
    private lateinit var adminEmail: String
    @Value("\${app.apc.admin.password}")
    private lateinit var adminPassword: String

    override fun run(vararg args: String?) {

        // role initialization
        listOf(RoleName.ROLE_USER, RoleName.ROLE_ADMIN)
            .forEach {
                roleRepository
                    .findRoleByName(it)
                    .getOrNull() ?: run {
                        val role = Role()
                        role.name = it
                        roleRepository.save(role)
                }
            }

        // admin account initialization
        if (userRepository.findByEmail(adminEmail).isEmpty) {
            userRepository.save(User().apply {
                email = adminEmail
                password = passwordEncoder.encode(adminPassword)
                roles = listOf(roleRepository.findRoleByName(RoleName.ROLE_ADMIN).get())
            })
        }

    }

}