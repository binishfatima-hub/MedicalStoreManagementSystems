package com.example.medicarebackend.controller;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.dto.ChangePasswordRequest;
import com.example.medicarebackend.model.Admin;
import com.example.medicarebackend.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The admin's own profile.
 *
 *   GET /api/admins/{id}            profile
 *   PUT /api/admins/{id}            update name / email / mobile
 *   PUT /api/admins/{id}/password   change password
 */
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public ApiResponse one(@PathVariable Integer id) {

        return adminService.findById(id)
                .map(admin -> ApiResponse.ok("Found", admin))
                .orElse(ApiResponse.fail("Admin not found"));
    }

    @PutMapping("/{id}")
    public ApiResponse updateProfile(@PathVariable Integer id, @RequestBody Admin changes) {
        return adminService.updateProfile(id, changes);
    }

    @PutMapping("/{id}/password")
    public ApiResponse changePassword(@PathVariable Integer id,
                                      @RequestBody ChangePasswordRequest request) {
        return adminService.changePassword(id, request);
    }
}
