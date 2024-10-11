package org.meliapp.backend.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    @Column(unique = true)
    lateinit var meliId: String
    lateinit var title: String
    lateinit var thumbnail: String
    lateinit var price: BigDecimal
}