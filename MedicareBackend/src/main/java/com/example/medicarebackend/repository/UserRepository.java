package com.example.medicarebackend.repository;

import com.example.medicarebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /** Used at login - the email is the user id. */
    Optional<User> findByEmail(String email);

    /** Used at registration to stop duplicate emails. */
    boolean existsByEmail(String email);
}
