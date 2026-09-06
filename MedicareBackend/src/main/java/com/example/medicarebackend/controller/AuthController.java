package com.example.medicarebackend.controller;

import com.example.medicarebackend.dto.AdminLoginRequest;
import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.dto.RegisterRequest;
import com.example.medicarebackend.dto.UserLoginRequest;
import com.example.medicarebackend.service.AdminService;
import com.example.medicarebackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login and registration.
 *
 *   POST /api/auth/admin/login     admin panel login
 *   POST /api/auth/user/register   user registration page
 *   POST /api/auth/user/login      user panel login
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminService adminService;
    private final UserService userService;

    public AuthController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/admin/login")
    public ApiResponse adminLogin(@RequestBody AdminLoginRequest request) {
        return adminService.login(request.getUsername(), request.getPassword());
    }

    @PostMapping("/user/register")
    public ApiResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/user/login")
    public ApiResponse userLogin(@RequestBody UserLoginRequest request) {
        return userService.login(request.getEmail(), request.getPassword());
    }
}
