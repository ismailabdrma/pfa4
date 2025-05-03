package com.amn.controller;

import com.amn.dto.AuthRequest;
import com.amn.dto.AuthResponse;
import com.amn.dto.RegisterRequest;
import com.amn.entity.*;
import com.amn.entity.enums.Role;
import com.amn.repository.UserRepository;
import com.amn.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        Role selectedRole = request.getRole();
        User user;

        switch (selectedRole) {
            case DOCTOR:
                user = Doctor.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .specialty(request.getSpecialization())
                        .role(Role.DOCTOR)
                        .matricule(request.getMatricule())
                        .build();
                break;

            case PHARMACIST:
                user = Pharmacist.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .role(Role.PHARMACIST)
                        .matricule(request.getMatricule())
                        .build();
                break;

            case PATIENT:
                user = Patient.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .role(Role.PATIENT)
                        .cin(request.getCin())
                        .birthDate(request.getBirthDate())
                        .bloodType(request.getBloodType())
                        .emergencyContact(request.getEmergencyContact())
                        .allergies(request.getAllergies())
                        .chronicDiseases(request.getChronicDiseases())
                        .hasHeartProblem(request.getHasHeartProblem())
                        .hasSurgery(request.getHasSurgery())
                        .build();
                break;

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported role");
        }

        userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
