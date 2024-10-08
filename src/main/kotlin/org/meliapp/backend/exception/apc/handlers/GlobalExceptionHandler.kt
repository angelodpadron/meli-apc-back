package org.meliapp.backend.exception.apc.handlers
import io.jsonwebtoken.JwtException
import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.exception.apc.BookmarkNotFoundException
import org.meliapp.backend.exception.apc.ProductNotFoundException
import org.meliapp.backend.exception.apc.UserAlreadyRegisteredException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException::class, JwtException::class)
    fun handleAuthException(e: RuntimeException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse(null, e.localizedMessage))
    }

    @ExceptionHandler(UserAlreadyRegisteredException::class)
    fun handleUserAlreadyRegisteredException(e: UserAlreadyRegisteredException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse(null, e.localizedMessage))
    }

    @ExceptionHandler(ProductNotFoundException::class, BookmarkNotFoundException::class)
    fun handleNotFoundException(e: ProductNotFoundException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse(null, e.localizedMessage))
    }

}