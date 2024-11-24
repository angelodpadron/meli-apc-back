package org.meliapp.backend.service

import org.meliapp.backend.dto.management.UserBasicResume
import org.meliapp.backend.dto.management.top.ProductBookmarkCount
import org.meliapp.backend.dto.management.top.ProductSaleCount
import org.meliapp.backend.dto.management.top.UserPurchaseCount
import org.meliapp.backend.repository.BookmarkRepository
import org.meliapp.backend.repository.PurchaseRepository
import org.meliapp.backend.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val userRepository: UserRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val purchaseRepository: PurchaseRepository
) {

    val pageRequest = PageRequest.of(0, 5)

    fun getRegisteredUsers(): List<UserBasicResume> =
        userRepository.getUsersBasicResume()

    fun top5MostBookmarked(): List<ProductBookmarkCount> =
        bookmarkRepository.getMostBookmarked(pageRequest)

    fun top5MostSold(): List<ProductSaleCount> =
        purchaseRepository.getMostSold(pageRequest)

    fun top5Buyers(): List<UserPurchaseCount> =
        purchaseRepository.getTopBuyers(pageRequest)

    fun getBookmarkedProducts(): List<ProductBookmarkCount> =
        bookmarkRepository.getBookmarksBasicResume()
}
