package com.innowise.authorization.controller;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.entity.Role;
import com.innowise.authorization.service.AuthenticationService;
import com.innowise.authorization.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationUser> register(@RequestBody AuthenticationUser user) {
        authenticationService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> token(@RequestParam String username, @RequestParam String password) {
        Map<String, String> tokens = authenticationService.generateTokens(username, password);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String token) {
        Boolean validateToken = authenticationService.validateToken(token);
        return ResponseEntity.ok(validateToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshTokens(refreshToken));
    }
}
