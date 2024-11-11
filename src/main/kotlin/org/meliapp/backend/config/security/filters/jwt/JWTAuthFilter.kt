package org.meliapp.backend.config.security.filters.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.meliapp.backend.service.CustomUserDetailsService
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTAuthFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val jwtHelper: JWTHelper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val header = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token = header.substring("Bearer ".length)
            val username = jwtHelper.extractUsername(token)

            if (username != null && SecurityContextHolder.getContext().authentication == null) {

                val userDetails = userDetailsService.loadUserByUsername(username)

                if (jwtHelper.validateToken(token, userDetails)) {
                    val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            }

            filterChain.doFilter(request, response)

        } catch (e: AccessDeniedException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message ?: "")
        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.message ?: "")
        }

    }
}