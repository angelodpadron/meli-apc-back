package org.meliapp.backend.controller

import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.dto.bookmark.BookmarkRequestBody
import org.meliapp.backend.dto.bookmark.BookmarkResponse
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
    fun getBookmarks(): ResponseEntity<ApiResponse<List<BookmarkResponse>>> {
        return ResponseEntity.ok(ApiResponse(bookmarkService.getUserBookmarks()))
    }

    @PostMapping
    fun bookmarkProduct(@RequestBody request: BookmarkRequestBody): ResponseEntity<ApiResponse<BookmarkResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse(bookmarkService.bookmarkProduct(request)))
    }

    @PutMapping("/{bookmarkId}")
    fun editBookmark(
        @PathVariable bookmarkId: Long,
        @RequestBody request: BookmarkRequestBody
    ): ResponseEntity<ApiResponse<BookmarkResponse>> {
        return ResponseEntity.ok(ApiResponse(bookmarkService.editBookmark(bookmarkId, request)))
    }

    @DeleteMapping("/{bookmarkId}")
    fun deleteBookmark(@PathVariable bookmarkId: Long): ResponseEntity<ApiResponse<Any>> {
        bookmarkService.deleteBookmark(bookmarkId)
        return ResponseEntity.ok(ApiResponse(null, "Bookmark deleted"))
    }

}