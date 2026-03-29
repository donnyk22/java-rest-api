package com.github.donnyk22.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.donnyk22.models.entities.MstUsers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String SECRET;

    @Value("${app.jwt.ttl-minutes}")
    private Integer EXPIRATION;

    @Value("${app.jwt.mfa.ttl-minutes}")
    private Integer MFA_EXPIRATION;

    public String generateToken(MstUsers users, String sessionId) {
        return Jwts.builder()
                .setSubject(users.getId().toString())
                .claim("username", users.getUsername())
                .claim("email", users.getEmail())
                .claim("role", users.getRole())
                .claim("sessionId", sessionId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(EXPIRATION))))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public String generateMfaToken(MstUsers users, String sessionId) {
        return Jwts.builder()
                .setSubject(users.getId().toString())
                .claim("username", users.getUsername())
                .claim("email", users.getEmail())
                .claim("role", "MFA_CHECK")
                .claim("sessionId", sessionId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(MFA_EXPIRATION))))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: " + e.getMessage());
            return null;
        }
    }
}