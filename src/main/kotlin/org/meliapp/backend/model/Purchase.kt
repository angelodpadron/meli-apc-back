package org.meliapp.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    @ManyToOne
    lateinit var user: User
    @ManyToOne
    lateinit var product: Product
    var quantity: Int = 0
    @CreationTimestamp
    var purchaseDate: LocalDateTime = LocalDateTime.now()
    var totalPrice: BigDecimal = BigDecimal.ZERO
}