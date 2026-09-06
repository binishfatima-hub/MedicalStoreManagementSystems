package com.example.medicarebackend.dto;

/** What the user login page sends: email (the user id) + password. */
public class UserLoginRequest {

    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
