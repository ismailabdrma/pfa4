package com.amn.controller;

import com.amn.entity.User;
import com.amn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ GET /api/user/me — Get current logged-in user's profile
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    // ✅ GET /api/user/email?value=xxx@example.com — Get user by email (admin access)
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam("value") String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ⛔ Optional: You can add PUT /api/user/me to update user profile later if needed
}
