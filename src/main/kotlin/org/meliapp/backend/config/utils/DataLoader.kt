package org.meliapp.backend.config.utils

import org.meliapp.backend.model.Role
import org.meliapp.backend.model.RoleName
import org.meliapp.backend.repository.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class DataLoader(
    private val roleRepository: RoleRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {

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
    }

}