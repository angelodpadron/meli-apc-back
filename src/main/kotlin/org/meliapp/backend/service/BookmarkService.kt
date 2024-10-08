package org.meliapp.backend.service

import jakarta.transaction.Transactional
import org.meliapp.backend.dto.bookmark.BookmarkRequestBody
import org.meliapp.backend.dto.bookmark.BookmarkResponse
import org.meliapp.backend.exception.apc.BookmarkException
import org.meliapp.backend.model.Bookmark
import org.meliapp.backend.model.Product
import org.meliapp.backend.model.User
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

    fun getUserBookmarks(): List<BookmarkResponse> {
        val currentUser = authService.getUserAuthenticated()

        return bookmarkRepository
            .findAllByUserId(currentUser.id)
            .map {
                BookmarkResponse(
                    id = it.id,
                    meliId = it.product.meliId,
                    userId = currentUser.id,
                    comment = it.comment,
                    stars = it.stars
                )
            }
    }

    @Transactional
    fun bookmarkProduct(request: BookmarkRequestBody): BookmarkResponse {
        val currentUser = authService.getUserAuthenticated()
        val productResponse = meliSearchService.findById(request.meliId)

        val savedProduct = productRepository
            .findByMeliId(productResponse.id)
            .orElseGet {
                productRepository.save(Product().apply {
                    meliId = productResponse.id
                    title = productResponse.title
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

        return BookmarkResponse(bookmark.id, request.meliId, currentUser.id, bookmark.stars, bookmark.comment)

    }

    @Transactional
    fun editBookmark(bookmarkId: Long, request: BookmarkRequestBody): BookmarkResponse {
        val currentUser = authService.getUserAuthenticated()
        val bookmark = getBookmark(bookmarkId)

        checkIfCanEdit(bookmark, currentUser)

        bookmark.apply {
            stars = request.stars
            comment = request.comment
        }

        bookmarkRepository.save(bookmark)

        return BookmarkResponse(bookmark.id, request.meliId, currentUser.id, bookmark.stars, bookmark.comment)
    }

    @Transactional
    fun deleteBookmark(bookmarkId: Long) {
        val currentUser = authService.getUserAuthenticated()
        val bookmark = getBookmark(bookmarkId)

        checkIfCanEdit(bookmark, currentUser)

        bookmarkRepository.delete(bookmark)
    }

    private fun checkIfCanEdit(bookmark: Bookmark, currentUser: User) {
        if (bookmark.user.id != currentUser.id) throw BookmarkException("Cannot edit an unrelated bookmark")
    }

    private fun getBookmark(bookmarkId: Long): Bookmark = bookmarkRepository
        .findById(bookmarkId)
        .orElseThrow { BookmarkException("No marked product was found with id $bookmarkId") }


}