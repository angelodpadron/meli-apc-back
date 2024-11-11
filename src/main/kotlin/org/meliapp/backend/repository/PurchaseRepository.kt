package org.meliapp.backend.repository

import org.meliapp.backend.model.Purchase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PurchaseRepository : JpaRepository<Purchase, Long> {
    @Query("SELECT p FROM Purchase p WHERE p.user.id = :id")
    fun findByUserId(@Param("id") id: Long): List<Purchase>
}