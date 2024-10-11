package org.meliapp.backend.controller

import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.dto.bookmark.BookmarkDetails
import org.meliapp.backend.dto.bookmark.BookmarkRequestBody
import org.meliapp.backend.dto.bookmark.BookmarkSummary
import org.meliapp.backend.service.BookmarkService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookmarks")
class BookmarkController(
    private val bookmarkService: BookmarkService
) {

    @GetMapping
    fun getBookmarks(): ResponseEntity<ApiResponse<List<BookmarkSummary>>> {
        return ResponseEntity.ok(ApiResponse(bookmarkService.getUserBookmarks()))
    }

    @GetMapping("/{bookmarkId}")
    fun getBookmarkDetails(@PathVariable bookmarkId: Long): ResponseEntity<ApiResponse<BookmarkDetails>> {
        return ResponseEntity.ok(ApiResponse(bookmarkService.getBookmarkDetails(bookmarkId)))
    }

    @PostMapping
    fun bookmarkProduct(@RequestBody request: BookmarkRequestBody): ResponseEntity<ApiResponse<BookmarkDetails>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse(bookmarkService.bookmarkProduct(request)))
    }

    @PutMapping("/{bookmarkId}")
    fun editBookmark(
        @PathVariable bookmarkId: Long,
        @RequestBody request: BookmarkRequestBody
    ): ResponseEntity<ApiResponse<BookmarkDetails>> {
        return ResponseEntity.ok(ApiResponse(bookmarkService.editBookmark(bookmarkId, request)))
    }

    @DeleteMapping("/{bookmarkId}")
    fun deleteBookmark(@PathVariable bookmarkId: Long): ResponseEntity<ApiResponse<Any>> {
        bookmarkService.deleteBookmark(bookmarkId)
        return ResponseEntity.ok(ApiResponse(null, "Bookmark deleted"))
    }

}