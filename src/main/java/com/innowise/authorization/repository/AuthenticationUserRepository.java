package com.innowise.authorization.repository;

import com.innowise.authorization.entity.AuthenticationUser;
import com.innowise.authorization.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser, Long> {

    Optional<AuthenticationUser> findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE authentication_users SET username = :username, password = :password, role = :role " +
            "WHERE id = :id", nativeQuery = true)
    int updateUser(@Param("id") Long id, @Param("username") String username, @Param("password") String password
            , @Param("role") Role role);
}
