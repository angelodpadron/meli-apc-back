package org.meliapp.backend.controller

import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.dto.management.UserBasicResume
import org.meliapp.backend.dto.management.top.ProductBookmarkCount
import org.meliapp.backend.dto.management.top.ProductSaleCount
import org.meliapp.backend.dto.management.top.UserPurchaseCount
import org.meliapp.backend.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService
) {

    @GetMapping("/registered-users")
    fun getRegisteredUsers(): ResponseEntity<ApiResponse<List<UserBasicResume>>> =
        ResponseEntity.ok(ApiResponse(adminService.getRegisteredUsers()))

    @GetMapping("/top-five-bookmarked")
    fun getMostBookmarked(): ResponseEntity<ApiResponse<List<ProductBookmarkCount>>> =
        ResponseEntity.ok(ApiResponse(adminService.top5MostBookmarked()))

    @GetMapping("/top-five-sold")
    fun getTop5MostSold(): ResponseEntity<ApiResponse<List<ProductSaleCount>>> =
        ResponseEntity.ok(ApiResponse(adminService.top5MostSold()))

    @GetMapping("/top-five-purchasers")
    fun getTop5Buyers(): ResponseEntity<ApiResponse<List<UserPurchaseCount>>> =
        ResponseEntity.ok(ApiResponse(adminService.top5Buyers()))

}