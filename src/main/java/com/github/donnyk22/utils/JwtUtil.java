package com.github.donnyk22.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String SECRET;

    @Value("${app.jwt.ttl-minutes}")
    private Integer EXPIRATION;

    public String generateToken(Integer id, String username, String email, String role) {
        return Jwts.builder()
            .setSubject(id.toString())
            .claim("username", username)
            .claim("email", email)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(EXPIRATION))))
            .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
            .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}