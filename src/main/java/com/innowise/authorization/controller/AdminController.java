package com.innowise.authorization.controller;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<AuthenticationUser>> getAllAuthenticatedUsers() {
        List<AuthenticationUser> users = adminService.getAllAuthenticatedUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AuthenticationUser> getAuthenticatedUserById(@PathVariable Long id) {
        AuthenticationUser user = adminService.getAuthenticatedUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get/username/{username}")
    public ResponseEntity<AuthenticationUser> getAuthenticatedUserById(@PathVariable String username) {
        AuthenticationUser user = adminService.getAuthenticatedUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<AuthenticationUser> createUser(@RequestBody AuthenticationUser userWithRole) {
        adminService.createUser(userWithRole);
        return ResponseEntity.ok(userWithRole);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AuthenticationUser> updateUser(@PathVariable Long id,
                                                         @RequestBody AuthenticationUser userWithRole) {
        adminService.updateUser(id, userWithRole);
        return ResponseEntity.ok(userWithRole);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername (@PathVariable String username) {
        adminService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
