package com.innowise.authorization.service;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.entity.Role;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
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
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    public AuthenticationUser getUserFromToken(String token) {
        Claims claims = validateToken(token).getBody();
        AuthenticationUser user = new AuthenticationUser();
        user.setUsername(claims.getSubject());
        user.setRole(Enum.valueOf(Role.class, claims.get("role", String.class)));
        return user;
    }
}
