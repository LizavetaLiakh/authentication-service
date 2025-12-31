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

    Optional<AuthenticationUser> findByEmail(String email);
    void deleteByEmail(String email);

    @Modifying
    @Query(value = "UPDATE authentication_users SET email = :email, password = :password, role = :role " +
            "WHERE id = :id", nativeQuery = true)
    int updateUser(@Param("id") Long id, @Param("email") String email, @Param("password") String password
            , @Param("role") String role);
}
