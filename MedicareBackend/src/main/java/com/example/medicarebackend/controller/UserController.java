package com.example.medicarebackend.controller;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.dto.ChangePasswordRequest;
import com.example.medicarebackend.model.User;
import com.example.medicarebackend.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Users.
 *
 * The admin panel uses the list / status / delete endpoints,
 * the user panel uses the profile and password endpoints.
 *
 *   GET    /api/users                    all users (admin)
 *   GET    /api/users/{id}               one user
 *   PUT    /api/users/{id}               update own name / mobile
 *   PUT    /api/users/{id}/password      change own password
 *   PUT    /api/users/{id}/status?status=ACTIVE|BLOCKED   (admin)
 *   DELETE /api/users/{id}               delete a user (admin)
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> all() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ApiResponse one(@PathVariable Integer id) {

        return userService.findById(id)
                .map(user -> ApiResponse.ok("Found", user))
                .orElse(ApiResponse.fail("User not found"));
    }

    @PutMapping("/{id}")
    public ApiResponse updateProfile(@PathVariable Integer id, @RequestBody User changes) {
        return userService.updateProfile(id, changes);
    }

    @PutMapping("/{id}/password")
    public ApiResponse changePassword(@PathVariable Integer id,
                                      @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(id, request);
    }

    @PutMapping("/{id}/status")
    public ApiResponse setStatus(@PathVariable Integer id, @RequestParam String status) {
        return userService.setStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Integer id) {
        return userService.delete(id);
    }
}
