package org.meliapp.backend.config.security.filters.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.meliapp.backend.model.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JWTHelper {


        private val SECRET_KEY = Keys.hmacShaKeyFor(ByteArray(32).also { SecureRandom().nextBytes(it) })
        private val MINUTES = 60

        fun generateToken(user: User): String {
            val instant = Instant.now()
            return Jwts.builder()
                .subject(user.email)
                .issuedAt(Date.from(instant))
                .expiration(Date.from(instant.plus(MINUTES.toLong(), ChronoUnit.MINUTES)))
                .claim("roles", user.roles.joinToString(",") { it.name.name })
                .signWith(SECRET_KEY)
                .compact()
        }

        fun validateToken(token: String, user: UserDetails): Boolean {
            return extractUsername(token) == user.username && !isTokenExpired(token)
        }

        fun extractUsername(token: String): String? {
            return extractTokenBody(token).subject
        }

        private fun extractTokenBody(token: String): Claims {
            return Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .payload
        }

        private fun isTokenExpired(token: String): Boolean {
            val claims = extractTokenBody(token)
            return claims.expiration.before(Date.from(Instant.now()))
        }




}