package com.innowise.authorization.service;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.exception.AuthorizedUserNotFoundException;
import com.innowise.authorization.exception.UserWithUsernameNotFoundException;
import com.innowise.authorization.repository.AuthenticationUserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final AuthenticationUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AuthenticationUserRepository repository, JwtService jwtService,
                        PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(AuthenticationUser user) {
        repository.findByUsername(user.getUsername())
                .ifPresent(sameEmailUser -> {
                    throw new UserWithUsernameNotFoundException(user.getUsername());
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    public AuthenticationUser getAuthenticatedUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AuthorizedUserNotFoundException(id));
    }

    public AuthenticationUser getAuthenticatedUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserWithUsernameNotFoundException(username));
    }

    public List<AuthenticationUser> getAllAuthenticatedUsers() {
        return repository.findAll();
    }

    @Transactional
    public void updateUser(Long id, AuthenticationUser user) {
        repository.findByUsername(user.getUsername())
                .ifPresent(sameUsernameUser -> {
                    if (!sameUsernameUser.getId().equals(id)) {
                        throw new UserWithUsernameNotFoundException(user.getUsername());
                    }
                });
        int updated = repository.updateUser(id, user.getUsername(), passwordEncoder.encode(user.getPassword()),
                user.getRole().name());
        if (updated == 0) {
            throw new AuthorizedUserNotFoundException(id);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorizedUserNotFoundException(id);
        }
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        try {
            repository.deleteByUsername(username);
        } catch (EmptyResultDataAccessException e) {
            throw new UserWithUsernameNotFoundException(username);
        }
    }
}
