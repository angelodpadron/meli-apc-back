package org.meliapp.backend.repository

import org.meliapp.backend.model.Role
import org.meliapp.backend.model.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByName(name: RoleName): Optional<Role>
}