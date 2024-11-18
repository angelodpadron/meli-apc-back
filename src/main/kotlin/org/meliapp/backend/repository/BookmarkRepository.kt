package org.meliapp.backend.repository

import org.meliapp.backend.dto.management.top.ProductBookmarkCount
import org.meliapp.backend.model.Bookmark
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findByUserId(@Param("userId") userId: Long): List<Bookmark>

    @Query("SELECT b FROM Bookmark b WHERE b.id = :bookmarkId AND b.user.id = :userId")
    fun findByIdAndUserId(@Param("bookmarkId") id: Long, @Param("userId") userId: Long): Optional<Bookmark>

    @Query("""
        SELECT new org.meliapp.backend.dto.management.top.ProductBookmarkCount(p.meliId, p.title, COUNT(p))
        FROM Bookmark b JOIN b.product p
        GROUP BY p.meliId, p.title
        ORDER BY COUNT(p) DESC
    """)
    fun getMostBookmarked(pageable: Pageable): List<ProductBookmarkCount>
}