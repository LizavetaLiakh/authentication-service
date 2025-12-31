package com.innowise.authorization.service;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.exception.AuthorizedUserNotFoundException;
import com.innowise.authorization.exception.UserWithEmailNotFoundException;
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
        repository.findByEmail(user.getEmail())
                .ifPresent(sameEmailUser -> {
                    throw new UserWithEmailNotFoundException(user.getEmail());
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    public AuthenticationUser getAuthenticatedUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AuthorizedUserNotFoundException(id));
    }

    public AuthenticationUser getAuthenticatedUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email));
    }

    public List<AuthenticationUser> getAllAuthenticatedUsers() {
        return repository.findAll();
    }

    @Transactional
    public void updateUser(Long id, AuthenticationUser user) {
        repository.findByEmail(user.getEmail())
                .ifPresent(sameUsernameUser -> {
                    if (!sameUsernameUser.getId().equals(id)) {
                        throw new UserWithEmailNotFoundException(user.getEmail());
                    }
                });
        int updated = repository.updateUser(id, user.getEmail(), passwordEncoder.encode(user.getPassword()),
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
    public void deleteUserByEmail(String email) {
        try {
            repository.deleteByEmail(email);
        } catch (EmptyResultDataAccessException e) {
            throw new UserWithEmailNotFoundException(email);
        }
    }
}
