package com.example.medicarebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * One row of the "users" table = one registered customer.
 * The email address is the login id.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    /** Login id. The database keeps this UNIQUE. */
    private String email;

    private String mobile;

    /** SHA-256 hash. @JsonIgnore keeps it out of every API response. */
    @JsonIgnore
    private String password;

    /** ACTIVE or BLOCKED - the admin can block a user. */
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
