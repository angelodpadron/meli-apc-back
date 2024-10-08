package org.meliapp.backend.controller

import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.dto.meli.MeliSearchResponse
import org.meliapp.backend.dto.product.ProductResponse
import org.meliapp.backend.service.MeliSearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class MeliSearchController(private val meliSearchService: MeliSearchService) {

    @GetMapping("/search")
    fun findByKeyword(@RequestParam keyword: String, @RequestParam(required = false) filters: Map<String, String>): ResponseEntity<ApiResponse<MeliSearchResponse>> {
        return ResponseEntity.ok(ApiResponse(meliSearchService.findByKeyword(keyword, filters)))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<ApiResponse<ProductResponse>> {
        return ResponseEntity.ok(ApiResponse(meliSearchService.findById(id)))
    }

}