package org.meliapp.backend.repository

import org.meliapp.backend.dto.management.UserBasicResume
import org.meliapp.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean

    @Query("""
        SELECT new org.meliapp.backend.dto.management.UserBasicResume(u.id, u.email, u.createdAt)
        FROM User u INNER JOIN u.roles r 
        WHERE r.name = 'ROLE_USER'
    """)
    fun getUsersBasicResume(): List<UserBasicResume>
}