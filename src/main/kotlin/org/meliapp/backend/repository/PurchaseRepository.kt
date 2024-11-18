package org.meliapp.backend.repository

import org.meliapp.backend.dto.management.top.ProductSaleCount
import org.meliapp.backend.dto.management.top.UserPurchaseCount
import org.meliapp.backend.model.Purchase
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PurchaseRepository : JpaRepository<Purchase, Long> {
    fun findByUserId(@Param("id") id: Long): List<Purchase>

    @Query("""
        SELECT new org.meliapp.backend.dto.management.top.ProductSaleCount(p.meliId, p.title, SUM(pr.quantity))
        FROM Purchase pr JOIN pr.product p 
        GROUP BY p.meliId, p.title 
        ORDER BY SUM(pr.quantity) DESC
    """)
    fun getMostSold(pageable: Pageable): List<ProductSaleCount>

    @Query("""
        SELECT new org.meliapp.backend.dto.management.top.UserPurchaseCount(u.email, SUM(p.totalPrice), SUM(p.quantity))
        FROM Purchase p JOIN p.user u
        GROUP BY u.email
        ORDER BY COUNT(p) DESC
    """)
    fun getTopBuyers(pageable: Pageable): List<UserPurchaseCount>
}