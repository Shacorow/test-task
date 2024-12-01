package com.test.task.config

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import java.util.*

object JwtTokenUtil {
    val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)
    private const val EXPIRATION_TIME = 3600000

    fun generateToken(username: String, roles: List<String>): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .claim("roles", roles)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun getClaimsFromToken(token: String): Claims {
        val parser = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
        return parser.parseClaimsJws(token).body
    }

    fun getUsernameFromToken(token: String): String {
        return getClaimsFromToken(token).subject
    }

    fun getRolesFromToken(token: String): List<String> {
        return getClaimsFromToken(token)["roles"] as List<String>
    }

    fun isTokenExpired(token: String): Boolean {
        return getClaimsFromToken(token).expiration.before(Date())
    }

    fun validateToken(token: String, username: String): Boolean {
        return username == getUsernameFromToken(token) && !isTokenExpired(token)
    }
}
