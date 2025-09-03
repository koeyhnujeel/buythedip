package com.zunza.buythedip.auth.jwt

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

private val logger = KotlinLogging.logger {  }

@Component
class JwtTokenProvider(
    @Value("\${jwt.key}")
    private val key: String,

    @Value("\${jwt.access-token-expire-time}")
    private val accessTokenExpireTime: Long,

    @Value("\${jwt.refresh-token-expire-time}")
    private val refreshTokenExpireTime: Long
) {
    fun generateAccessJwt(
        userId: Long,
        roles: Collection<GrantedAuthority?>?
    ): String {
        val authorities = roles?.map { it?.authority }
        val key = getKey()
        val now = Instant.now()

        return Jwts.builder()
            .subject(userId.toString())
            .claim("auth", authorities)
            .issuedAt(Date(now.toEpochMilli()))
            .expiration(Date(now.plusMillis(accessTokenExpireTime).toEpochMilli()))
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun generateRefreshJwt(userId: Long): String {
        val key = getKey()
        val now = Instant.now()

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date(now.toEpochMilli()))
            .expiration(Date(now.plusMillis(refreshTokenExpireTime).toEpochMilli()))
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun validateJwt(jwt: String?): Boolean {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(jwt)
            return true
        } catch (e: ExpiredJwtException) {
            logger.warn { "JWT가 만료되었습니다." }
            throw JwtException("JWT가 만료되었습니다.")
        } catch (e: SignatureException) {
            logger.warn { "JWT 서명 검증에 실패했습니다." }
            throw JwtException("JWT 서명 검증에 실패했습니다.")
        }
    }

    fun getAuthentication(jwt: String): Authentication {
        val claims = getClaims(jwt)
        val userId = claims.subject.toLong()
        val authorities = claims["auth"] as? List<*>

        return UsernamePasswordAuthenticationToken(userId,
            null,
            authorities?.filterIsInstance<String>()
                ?.map { SimpleGrantedAuthority(it) })
    }

    private fun getKey(): SecretKey {
        return Keys.hmacShaKeyFor(this.key.toByteArray())
    }

    fun getClaims(jwt: String): Claims {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(jwt).payload
    }
}
