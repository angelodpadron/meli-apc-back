package org.meliapp.backend.repository

import org.meliapp.backend.model.Bookmark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.user.id = :userId")
    fun findByUserId(@Param("userId") userId: Long): List<Bookmark>

    @Query("SELECT b FROM Bookmark b WHERE b.user.id = :userId AND b.id = :bookmarkId")
    fun findByIdAndUserId(@Param("userId") userId: Long, @Param("bookmarkId") id: Long): Optional<Bookmark>
}