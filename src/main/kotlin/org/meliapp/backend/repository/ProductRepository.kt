package org.meliapp.backend.repository

import org.meliapp.backend.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByMeliId(meliId: String): Optional<Product>
}