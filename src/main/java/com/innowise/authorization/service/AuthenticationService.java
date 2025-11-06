package com.innowise.authorization.service;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.entity.Role;
import com.innowise.authorization.repository.AuthenticationUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final AuthenticationUserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthenticationUserRepository repository, JwtService jwtService,
                                 PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(AuthenticationUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    public Map<String, String> generateTokens(String username, String password) {
        AuthenticationUser user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User " + username + " not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    public boolean validateToken(String token) {
        try {
            jwtService.validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, String> refreshTokens(String refreshToken) {
        AuthenticationUser user = jwtService.getUserFromToken(refreshToken);
        String newAccess = jwtService.generateToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", newAccess);
        tokens.put("refresh_token", newRefresh);
        return tokens;
    }
}
