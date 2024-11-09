package org.meliapp.backend.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.meliapp.backend.dto.bookmark.BookmarkRequestBody
import org.meliapp.backend.exception.apc.BookmarkNotFoundException
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.meliapp.backend.model.Bookmark
import org.meliapp.backend.model.Product
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.BookmarkRepository
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
class BookmarkServiceUnitTest {

    @Mock
    private lateinit var authService: AuthService

    @Mock
    private lateinit var bookmarkRepository: BookmarkRepository

    @Mock
    private lateinit var productService: ProductService

    @InjectMocks
    private lateinit var bookmarkService: BookmarkService

    @Test
    fun `should create bookmark of an existing product`() {
        // Arrange
        val bookmarkRequest = BookmarkRequestBody("meli_id", 5, "comment")

        val mockUser: User = mock()
        val mockProduct: Product = mock()

        whenever(mockUser.id).thenReturn(1L)
        whenever(mockProduct.meliId).thenReturn("meli_id")
        whenever(mockProduct.title).thenReturn("product_title")
        whenever(mockProduct.thumbnail).thenReturn("product_thumbnail")

        whenever(productService.findByMeliId(bookmarkRequest.meliId)).thenReturn(mockProduct)
        whenever(authService.getUserAuthenticated()).thenReturn(mockUser)

        // Act
        val response = bookmarkService.bookmarkProduct(bookmarkRequest)

        // Assert
        assertEquals(bookmarkRequest.meliId, response.postId)
        assertEquals(bookmarkRequest.stars, response.stars)
        assertEquals(bookmarkRequest.comment, response.comment)

        // Verify
        verify(bookmarkRepository, times(1)).save(any())

    }

    @Test
    fun `should throw exception when bookmarking with wrong meli id`() {
        // Arrange
        val bookmarkRequest = BookmarkRequestBody("meli_id", 5, "comment")
        val mockUser: User = mock()

        whenever(mockUser.id).thenReturn(1L)
        whenever(authService.getUserAuthenticated()).thenReturn(mockUser)
        whenever(productService.findByMeliId(anyString())).thenThrow(ProductNotFoundException::class.java)

        // Act and Assert
        assertThrows<ProductNotFoundException> { bookmarkService.bookmarkProduct(bookmarkRequest) }
    }

    @Test
    fun `should return a list of bookmarks from user`() {
        // Arrange
        val mockUser: User = mock()
        whenever(mockUser.id).thenReturn(1L)
        whenever(authService.getUserAuthenticated()).thenReturn(mockUser)

        // Act
        val response = bookmarkService.getUserBookmarks()

        // Assert
        assertTrue(response.isEmpty())
    }

    @Test
    fun `should delete an existing bookmark`() {
        // Arrange
        val bookmarkId = 1L
        val userId = 1L
        val bookmark: Bookmark = mock()
        val user: User = mock()

        whenever(user.id).thenReturn(userId)
        whenever(authService.getUserAuthenticated()).thenReturn(user)
        whenever(bookmarkRepository.findByIdAndUserId(userId, bookmarkId)).thenReturn(Optional.of(bookmark))

        // Act
        bookmarkService.deleteBookmark(bookmarkId)

        // Verify
        verify(bookmarkRepository, times(1)).delete(bookmark)

    }

    @Test
    fun `should throw an exception when attempting to delete a non existing bookmark`() {
        // Arrange
        val bookmarkId = 1L
        val userId = 1L
        val user: User = mock()

        whenever(bookmarkRepository.findByIdAndUserId(userId, bookmarkId)).thenReturn(Optional.empty())
        whenever(user.id).thenReturn(userId)
        whenever(authService.getUserAuthenticated()).thenReturn(user)

        // Act and Assert
        assertThrows<BookmarkNotFoundException> { bookmarkService.deleteBookmark(bookmarkId) }
    }

    @Test
    fun `should edit bookmark`() {
        // Arrange
        val request = BookmarkRequestBody("meli_id", 4, "updated comment")
        val mockUser: User = mock()
        val mockProduct: Product = mock()

        val bookmark = Bookmark().apply {
            id = 1L
            stars = 5
            product = mockProduct
            comment = "old comment"
            user = mockUser
        }

        whenever(bookmarkRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(bookmark))
        whenever(authService.getUserAuthenticated()).thenReturn(mockUser)
        whenever(mockProduct.meliId).thenReturn("meli_id")
        whenever(mockProduct.title).thenReturn("product_title")
        whenever(mockProduct.thumbnail).thenReturn("product_thumbnail")
        whenever(mockUser.id).thenReturn(1L)

        // Act
        val response = bookmarkService.editBookmark(bookmark.id, request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("updated comment", response.comment)
        assertEquals(4, response.stars)
        assertEquals("meli_id", response.postId)

        // Verify
        verify(bookmarkRepository, times(1)).save(bookmark)
    }


}