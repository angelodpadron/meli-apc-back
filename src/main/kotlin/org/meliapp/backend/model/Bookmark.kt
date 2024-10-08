package org.meliapp.backend.model

import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "product_id"])])
class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    lateinit var product: Product

    @ManyToOne
    lateinit var user: User

    var stars: Int = 0
    var comment: String = ""

}