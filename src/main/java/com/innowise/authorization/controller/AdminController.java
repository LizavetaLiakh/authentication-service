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

    @GetMapping("/get/email/{email}")
    public ResponseEntity<AuthenticationUser> getAuthenticatedUserByEmail(@PathVariable String email) {
        AuthenticationUser user = adminService.getAuthenticatedUserByEmail(email);
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
        AuthenticationUser updatedUser = adminService.getAuthenticatedUserById(id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/email/{email}")
    public ResponseEntity<Void> deleteUserByUsername (@PathVariable String email) {
        adminService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/internal/delete/username/{username}")
    public ResponseEntity<Void> deleteUserInternally(@PathVariable String username) {
        adminService.deleteUserByEmail(username);
        return ResponseEntity.noContent().build();
    }
}
