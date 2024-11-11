package org.meliapp.backend.controller

import io.swagger.v3.oas.annotations.Operation
import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.dto.auth.AuthRequestBody
import org.meliapp.backend.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["http:localhost:4200"])
class AuthController(
    private val authService: AuthService
) {
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    fun register(@RequestBody authRequestBody: AuthRequestBody): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse(authService.register(authRequestBody)))
    }

    @Operation(summary = "Log in")
    @PostMapping("/login")
    fun login(@RequestBody authRequestBody: AuthRequestBody): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity.ok(ApiResponse(authService.login(authRequestBody)))
    }

}