package org.meliapp.backend.controller

import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.dto.purchase.PurchaseRequest
import org.meliapp.backend.dto.purchase.PurchaseResponse
import org.meliapp.backend.service.PurchaseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/purchases")
class PurchaseController(
    private val purchaseService: PurchaseService
) {

    @PostMapping
    fun buy(@RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<ApiResponse<PurchaseResponse>> {
        return ResponseEntity.ok(ApiResponse(purchaseService.buy(purchaseRequest)))
    }

    @GetMapping
    fun purchases(): ResponseEntity<ApiResponse<List<PurchaseResponse>>> {
        return ResponseEntity.ok(ApiResponse(purchaseService.purchases()))
    }

}