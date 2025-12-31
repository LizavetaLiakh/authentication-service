package com.innowise.authorization.service;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.entity.Role;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(AuthenticationUser user) {
        return buildToken(user, accessExpiration);
    }

    public String generateRefreshToken(AuthenticationUser user) {
        return buildToken(user, refreshExpiration);
    }

    private String buildToken(AuthenticationUser user, long expiration) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public AuthenticationUser getUserFromToken(String token) {
        Claims claims = parseToken(token);
        AuthenticationUser user = new AuthenticationUser();
        user.setEmail(claims.getSubject());
        user.setRole(Role.valueOf(claims.get("role", String.class)));
        return user;
    }
}
