package org.meliapp.backend.service

import jakarta.transaction.Transactional
import org.meliapp.backend.dto.bookmark.BookmarkDetails
import org.meliapp.backend.dto.bookmark.BookmarkRequestBody
import org.meliapp.backend.dto.bookmark.BookmarkSummary
import org.meliapp.backend.exception.apc.BookmarkNotFoundException
import org.meliapp.backend.model.Bookmark
import org.meliapp.backend.model.Product
import org.meliapp.backend.repository.BookmarkRepository
import org.meliapp.backend.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class BookmarkService(
    private val meliSearchService: MeliSearchService,
    private val authService: AuthService,
    private val bookmarkRepository: BookmarkRepository,
    private val productRepository: ProductRepository,
) {

    fun getUserBookmarks(): List<BookmarkSummary> {
        val currentUser = authService.getUserAuthenticated()

        return bookmarkRepository
            .findByUserId(currentUser.id)
            .map { toBookmarkSummary(it) }
    }

    fun getBookmarkDetails(bookmarkId: Long): BookmarkDetails {
        val currentUser = authService.getUserAuthenticated()
        return toBookmarkDetails(getBookmark(bookmarkId, currentUser.id))
    }

    private fun toBookmarkDetails(bookmark: Bookmark): BookmarkDetails {
        return BookmarkDetails(
            id = bookmark.id,
            productTitle = bookmark.product.title,
            postId = bookmark.product.meliId,
            thumbnail = bookmark.product.thumbnail,
            stars = bookmark.stars,
            comment = bookmark.comment,

            )
    }

    @Transactional
    fun bookmarkProduct(request: BookmarkRequestBody): BookmarkDetails {
        val currentUser = authService.getUserAuthenticated()
        val productResponse = meliSearchService.findById(request.meliId)

        val savedProduct = productRepository
            .findByMeliId(productResponse.id)
            .orElseGet {
                productRepository.save(Product().apply {
                    meliId = productResponse.id
                    title = productResponse.title
                    thumbnail = productResponse.thumbnail
                    price = productResponse.price
                })
            }

        val bookmark = Bookmark().apply {
            product = savedProduct
            stars = request.stars
            comment = request.comment
            user = currentUser
        }

        bookmarkRepository.save(bookmark)

        return toBookmarkDetails(bookmark)

    }

    @Transactional
    fun editBookmark(bookmarkId: Long, request: BookmarkRequestBody): BookmarkDetails {
        val currentUser = authService.getUserAuthenticated()
        val bookmark = getBookmark(bookmarkId, currentUser.id)

        bookmark.apply {
            stars = request.stars
            comment = request.comment
        }

        bookmarkRepository.save(bookmark)

        return toBookmarkDetails(bookmark)
    }

    @Transactional
    fun deleteBookmark(bookmarkId: Long) {
        val currentUser = authService.getUserAuthenticated()
        val bookmark = getBookmark(bookmarkId, currentUser.id)

        bookmarkRepository.delete(bookmark)
    }

    private fun getBookmark(bookmarkId: Long, userId: Long): Bookmark = bookmarkRepository
        .findByIdAndUserId(bookmarkId, userId)
        .orElseThrow { BookmarkNotFoundException(bookmarkId) }

    private fun toBookmarkSummary(bookmark: Bookmark) =
        BookmarkSummary(
            id = bookmark.id,
            productTitle = bookmark.product.title,
            thumbnail = bookmark.product.thumbnail,
            stars = bookmark.stars
        )


}