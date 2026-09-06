package com.example.medicarebackend.service;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.dto.ChangePasswordRequest;
import com.example.medicarebackend.dto.RegisterRequest;
import com.example.medicarebackend.model.User;
import com.example.medicarebackend.repository.UserRepository;
import com.example.medicarebackend.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/** Registration, login and password rules for normal users (customers). */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registration. There is no email verification - as soon as the form
     * is valid the user is saved and can log in straight away.
     */
    public ApiResponse register(RegisterRequest request) {

        String name = trim(request.getName());
        String email = trim(request.getEmail()).toLowerCase();
        String mobile = trim(request.getMobile());
        String password = request.getPassword();

        /* ---------- validation ---------- */

        if (isBlank(name)) {
            return ApiResponse.fail("Please enter your name");
        }

        if (isBlank(email) || !email.contains("@") || !email.contains(".")) {
            return ApiResponse.fail("Please enter a valid email address");
        }

        if (mobile == null || !mobile.matches("[0-9]{10}")) {
            return ApiResponse.fail("Mobile number must be exactly 10 digits");
        }

        if (isBlank(password) || password.length() < 4) {
            return ApiResponse.fail("Password must be at least 4 characters");
        }

        if (!password.equals(request.getConfirmPassword())) {
            return ApiResponse.fail("Password and confirm password do not match");
        }

        if (userRepository.existsByEmail(email)) {
            return ApiResponse.fail("This email is already registered. Please login.");
        }

        /* ---------- save ---------- */

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPassword(PasswordUtil.hash(password));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ApiResponse.ok("Registration successful! You can login now.", user);
    }

    /** Login with email + password. */
    public ApiResponse login(String email, String password) {

        if (isBlank(email) || isBlank(password)) {
            return ApiResponse.fail("Please enter email and password");
        }

        Optional<User> found =
                userRepository.findByEmail(trim(email).toLowerCase());

        if (found.isEmpty()) {
            return ApiResponse.fail("Invalid email or password");
        }

        User user = found.get();

        if (!PasswordUtil.matches(password, user.getPassword())) {
            return ApiResponse.fail("Invalid email or password");
        }

        if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
            return ApiResponse.fail("Your account has been blocked by the admin");
        }

        return ApiResponse.ok("Login successful", user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    /** Change password screen in the user panel. */
    public ApiResponse changePassword(Integer userId, ChangePasswordRequest request) {

        Optional<User> found = userRepository.findById(userId);

        if (found.isEmpty()) {
            return ApiResponse.fail("User not found");
        }

        User user = found.get();

        if (!PasswordUtil.matches(request.getOldPassword(), user.getPassword())) {
            return ApiResponse.fail("Current password is wrong");
        }

        String newPassword = request.getNewPassword();

        if (isBlank(newPassword) || newPassword.length() < 4) {
            return ApiResponse.fail("New password must be at least 4 characters");
        }

        if (!newPassword.equals(request.getConfirmPassword())) {
            return ApiResponse.fail("New password and confirm password do not match");
        }

        user.setPassword(PasswordUtil.hash(newPassword));
        userRepository.save(user);

        return ApiResponse.ok("Password updated successfully");
    }

    /** The user edits their own name / mobile (email cannot change - it is the id). */
    public ApiResponse updateProfile(Integer userId, User changes) {

        Optional<User> found = userRepository.findById(userId);

        if (found.isEmpty()) {
            return ApiResponse.fail("User not found");
        }

        User user = found.get();

        if (!isBlank(changes.getName())) {
            user.setName(changes.getName().trim());
        }

        if (changes.getMobile() != null) {

            if (!changes.getMobile().matches("[0-9]{10}")) {
                return ApiResponse.fail("Mobile number must be exactly 10 digits");
            }

            user.setMobile(changes.getMobile().trim());
        }

        userRepository.save(user);

        return ApiResponse.ok("Profile updated successfully", user);
    }

    /** Admin action: block or unblock a customer. */
    public ApiResponse setStatus(Integer userId, String status) {

        Optional<User> found = userRepository.findById(userId);

        if (found.isEmpty()) {
            return ApiResponse.fail("User not found");
        }

        if (!"ACTIVE".equalsIgnoreCase(status) && !"BLOCKED".equalsIgnoreCase(status)) {
            return ApiResponse.fail("Status must be ACTIVE or BLOCKED");
        }

        User user = found.get();
        user.setStatus(status.toUpperCase());
        userRepository.save(user);

        return ApiResponse.ok("User is now " + user.getStatus(), user);
    }

    /** Admin action: delete a customer account. */
    public ApiResponse delete(Integer userId) {

        if (!userRepository.existsById(userId)) {
            return ApiResponse.fail("User not found");
        }

        userRepository.deleteById(userId);

        return ApiResponse.ok("User deleted successfully");
    }

    private String trim(String text) {
        return text == null ? null : text.trim();
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
