package com.amn.controller;

import com.amn.dto.AuthRequest;
import com.amn.dto.AuthResponse;
import com.amn.dto.RegisterRequest;
import com.amn.entity.Doctor;
import com.amn.entity.Patient;
import com.amn.entity.Pharmacist;
import com.amn.entity.User;
import com.amn.entity.enums.Role;
import com.amn.repository.UserRepository;
import com.amn.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user;

        switch (request.getRole()) {
            case DOCTOR:
                user = Doctor.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .specialty(request.getSpecialization())
                        .role(Role.DOCTOR)
                        .build();
                break;

            case PHARMACIST:
                user = Pharmacist.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.PHARMACIST)
                        .phone(request.getPhone())
                        .build();
                break;
            case PATIENT:
                user = Patient.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.PATIENT)
                        .phone(request.getPhone())
                        .build();
                break;


            default:
                throw new IllegalArgumentException("Only DOCTOR and PHARMACIST can register themselves");
        }

        userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
