package org.meliapp.backend.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.meliapp.backend.dto.bookmark.BookmarkRequestBody
import org.meliapp.backend.dto.product.ProductResponse
import org.meliapp.backend.exception.apc.BookmarkNotFoundException
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.meliapp.backend.model.Bookmark
import org.meliapp.backend.model.Product
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.BookmarkRepository
import org.meliapp.backend.repository.ProductRepository
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.*
import org.mockito.Mockito.`when` as whenever


@ExtendWith(SpringExtension::class)
class BookmarkServiceUnitTest {

    @Mock
    private lateinit var meliSearchService: MeliSearchService

    @Mock
    private lateinit var authService: AuthService

    @Mock
    private lateinit var bookmarkRepository: BookmarkRepository

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var bookmarkService: BookmarkService

    @Test
    fun `should create bookmark of an existing product`() {
        // Arrange
        val bookmarkRequest = BookmarkRequestBody("meli_id", 5, "comment")

        val productResponse = ProductResponse(
            id = bookmarkRequest.meliId,
            price = BigDecimal(1),
            title = "product_title",
            availableQuantity = 1,
            thumbnail = "product_thumbnail",
        )

        val mockUser = mock(User::class.java)
        val mockProduct = mock(Product::class.java)

        whenever(mockUser.id).thenReturn(1L)
        whenever(mockProduct.meliId).thenReturn("meli_id")

        whenever(meliSearchService.findById(bookmarkRequest.meliId)).thenReturn(productResponse)
        whenever(productRepository.findByMeliId(bookmarkRequest.meliId)).thenReturn(Optional.of(mockProduct))
        whenever(authService.getUserAuthenticated()).thenReturn(mockUser)

        // Act
        val response = bookmarkService.bookmarkProduct(bookmarkRequest)

        // Assert
        assertEquals(bookmarkRequest.meliId, response.meliId)
        assertEquals(bookmarkRequest.stars, response.stars)
        assertEquals(bookmarkRequest.comment, response.comment)
        assertEquals(mockUser.id, response.userId)

        // Verify
        verify(bookmarkRepository, times(1)).save(any())
        verify(productRepository, times(0)).save(any())

    }

    @Test
    fun `should not save an already persisted product`() {
        // Arrange
        val bookmarkRequest = BookmarkRequestBody("meli_id", 5, "comment")

        val productResponse = ProductResponse(
            id = bookmarkRequest.meliId,
            price = BigDecimal(1),
            title = "product_title",
            availableQuantity = 1,
            thumbnail = "product_thumbnail",
        )

        val mockUser = mock(User::class.java)
        val mockProduct = mock(Product::class.java)

        whenever(mockUser.id).thenReturn(1L)
        whenever(mockProduct.meliId).thenReturn("meli_id")

        whenever(meliSearchService.findById(bookmarkRequest.meliId)).thenReturn(productResponse)
        whenever(productRepository.findByMeliId(bookmarkRequest.meliId)).thenReturn(Optional.empty())
        whenever(productRepository.save(any())).thenReturn(mockProduct)
        whenever(authService.getUserAuthenticated()).thenReturn(mockUser)

        // Act
        bookmarkService.bookmarkProduct(bookmarkRequest)

        // Verify
        verify(productRepository, times(1)).save(any())
    }

    @Test
    fun `should throw exception when bookmarking with wrong meli id`() {
        // Arrange
        val bookmarkRequest = BookmarkRequestBody("meli_id", 5, "comment")
        val mockUser = mock(User::class.java)

        whenever(mockUser.id).thenReturn(1L)
        whenever(meliSearchService.findById(bookmarkRequest.meliId)).thenThrow(ProductNotFoundException::class.java)
        whenever(authService.getUserAuthenticated()).thenReturn(mockUser)

        // Act and Assert
        assertThrows<ProductNotFoundException> { bookmarkService.bookmarkProduct(bookmarkRequest) }
    }

    @Test
    fun `should return a list of bookmarks from user`() {
        // Arrange
        val mockUser = mock(User::class.java)
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
        val bookmark = mock(Bookmark::class.java)
        val user = mock(User::class.java)

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
        val user = mock(User::class.java)

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
        val mockUser = mock(User::class.java)
        val mockProduct = mock(Product::class.java)

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
        whenever(mockUser.id).thenReturn(1L)

        // Act
        val response = bookmarkService.editBookmark(bookmark.id, request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("updated comment", response.comment)
        assertEquals(4, response.stars)
        assertEquals("meli_id", response.meliId)
        assertEquals(1L, response.userId)

        // Verify
        verify(bookmarkRepository, times(1)).save(bookmark)
    }


}