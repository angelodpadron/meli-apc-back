package org.meliapp.backend.model

import jakarta.persistence.*

@Entity
class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    lateinit var name: RoleName

}