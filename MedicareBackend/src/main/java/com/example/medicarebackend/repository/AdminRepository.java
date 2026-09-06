package com.example.medicarebackend.repository;

import com.example.medicarebackend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    /** Used at login. Spring writes the SQL from the method name. */
    Optional<Admin> findByUsername(String username);
}
