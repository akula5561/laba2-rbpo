package com.danil.library.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, который умеет генерировать и валидировать JWT access / refresh токены.
 */
@Component
public class JwtTokenProvider {

    // Секретный ключ (для лабы — просто строка; в реале хранить в настройках/ENV)
    private static final String SECRET_KEY = "VerySecretJwtKeyForLibraryApp1234567890";

    // Сроки жизни токенов
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 15 * 60;      // 15 минут
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60; // 7 дней

    private final Key signingKey;

    public JwtTokenProvider() {
        byte[] keyBytes = SECRET_KEY.getBytes(); // можно Base64, но тут хватит и так
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public Instant getAccessTokenExpiryInstant() {
        return Instant.now().plusSeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    public Instant getRefreshTokenExpiryInstant() {
        return Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiry = getAccessTokenExpiryInstant();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .claim("roles", roles)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiry = getRefreshTokenExpiryInstant();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .claim("type", "refresh")
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
