package com.innowise.authorization.controller;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.exception.UserWithEmailNotFoundException;
import com.innowise.authorization.service.AuthenticationService;
import com.innowise.authorization.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthenticationUser user) {
        try {
            authenticationService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
        } catch (UserWithEmailNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
        }
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> token(@RequestParam String email, @RequestParam String password) {
        Map<String, String> tokens = authenticationService.generateTokens(email, password);
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
