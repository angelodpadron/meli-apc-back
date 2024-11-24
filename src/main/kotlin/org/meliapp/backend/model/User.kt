package org.meliapp.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long = 0
    lateinit var email: String
    lateinit var password: String
    @ManyToMany(fetch = FetchType.EAGER)
    lateinit var roles: Collection<Role>
    @CreationTimestamp
    lateinit var createdAt: LocalDateTime


}