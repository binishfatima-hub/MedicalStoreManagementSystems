package com.example.medicarebackend.service;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.dto.ChangePasswordRequest;
import com.example.medicarebackend.model.Admin;
import com.example.medicarebackend.repository.AdminRepository;
import com.example.medicarebackend.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** All the admin login / password rules live here. */
@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /** Step 1 - check the username, step 2 - check the password hash. */
    public ApiResponse login(String username, String password) {

        if (isBlank(username) || isBlank(password)) {
            return ApiResponse.fail("Please enter username and password");
        }

        Optional<Admin> found = adminRepository.findByUsername(username.trim());

        if (found.isEmpty()) {
            return ApiResponse.fail("Invalid username or password");
        }

        Admin admin = found.get();

        if (!PasswordUtil.matches(password, admin.getPassword())) {
            return ApiResponse.fail("Invalid username or password");
        }

        return ApiResponse.ok("Login successful", admin);
    }

    public Optional<Admin> findById(Integer id) {
        return adminRepository.findById(id);
    }

    /** Change password screen in the admin panel. */
    public ApiResponse changePassword(Integer adminId, ChangePasswordRequest request) {

        Optional<Admin> found = adminRepository.findById(adminId);

        if (found.isEmpty()) {
            return ApiResponse.fail("Admin not found");
        }

        Admin admin = found.get();

        if (!PasswordUtil.matches(request.getOldPassword(), admin.getPassword())) {
            return ApiResponse.fail("Current password is wrong");
        }

        String newPassword = request.getNewPassword();

        if (isBlank(newPassword) || newPassword.length() < 4) {
            return ApiResponse.fail("New password must be at least 4 characters");
        }

        if (!newPassword.equals(request.getConfirmPassword())) {
            return ApiResponse.fail("New password and confirm password do not match");
        }

        admin.setPassword(PasswordUtil.hash(newPassword));
        adminRepository.save(admin);

        return ApiResponse.ok("Password updated successfully");
    }

    /** Update the admin's own name / email / mobile. */
    public ApiResponse updateProfile(Integer adminId, Admin changes) {

        Optional<Admin> found = adminRepository.findById(adminId);

        if (found.isEmpty()) {
            return ApiResponse.fail("Admin not found");
        }

        Admin admin = found.get();

        if (!isBlank(changes.getName()))   admin.setName(changes.getName().trim());
        if (!isBlank(changes.getEmail()))  admin.setEmail(changes.getEmail().trim());
        if (!isBlank(changes.getMobile())) admin.setMobile(changes.getMobile().trim());

        adminRepository.save(admin);

        return ApiResponse.ok("Profile updated successfully", admin);
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
