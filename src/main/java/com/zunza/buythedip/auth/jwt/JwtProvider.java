package com.zunza.buythedip.auth.jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

	private final long accessTokenExpireTime;
	private final long refreshTokenExpireTime;
	private final SecretKey key;

	public JwtProvider(
		@Value("${jwt.key}") String secretKey,
		@Value("${jwt.access-token-expire-time}") long accessTokenExpireTime,
		@Value("${jwt.refresh-token-expire-time}") long refreshTokenExpireTime) {

		this.accessTokenExpireTime = accessTokenExpireTime;
		this.refreshTokenExpireTime = refreshTokenExpireTime;
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateAccessToken(
		Long userId,
		Collection<? extends GrantedAuthority> roles
	) {
		List<String> authorities = roles != null ?
			roles.stream()
				.map(GrantedAuthority::getAuthority)
				.toList()
			: List.of();

		Instant now = Instant.now();
		Instant expiration = now.plusMillis(accessTokenExpireTime);

		return Jwts.builder()
			.subject(userId.toString())
			.claim("auth", authorities)
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiration))
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public String generateRefreshToken(Long userId) {
		Instant now = Instant.now();
		Instant expiration = now.plusMillis(refreshTokenExpireTime);

		return Jwts.builder()
			.subject(userId.toString())
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiration))
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.warn("JWT가 만료되었습니다: {}", e.getMessage());
			throw new JwtException("JWT가 만료되었습니다.");
		} catch (SignatureException e) {
			log.warn("JWT 서명 검증에 실패했습니다: {}", e.getMessage());
			throw new JwtException("JWT 서명 검증에 실패했습니다.");
		} catch (MalformedJwtException e) {
			log.warn("잘못된 JWT 형식입니다: {}", e.getMessage());
			throw new JwtException("잘못된 JWT 형식입니다.");
		} catch (UnsupportedJwtException e) {
			log.warn("지원되지 않는 JWT입니다: {}", e.getMessage());
			throw new JwtException("지원되지 않는 JWT입니다.");
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);
		Long userId = Long.valueOf(claims.getSubject());
		List<String> authoritiesList = claims.get("auth", List.class);

		Collection<SimpleGrantedAuthority> authorities = authoritiesList != null ?
			authoritiesList.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList())
			: List.of();

		return new UsernamePasswordAuthenticationToken(userId, null, authorities);
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
