package com.amn.service;

import com.amn.dto.AuthResponse;
import com.amn.dto.RegisterRequest;
import com.amn.entity.*;
import com.amn.entity.enums.Role;
import com.amn.repository.UserRepository;
import com.amn.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.amn.entity.enums.Role.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        User user;

        switch (request.getRole()) {
            case DOCTOR -> user = Doctor.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .role(DOCTOR)
                    .build();

            case PATIENT -> user = Patient.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .role(PATIENT)
                    .cin(request.getCin())
                    .birthDate(request.getBirthDate())
                    .bloodType(request.getBloodType())
                    .build();

            case PHARMACIST -> user = Pharmacist.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .role(PHARMACIST)
                    .build();

            case ADMIN -> user = Admin.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .role(Role.ADMIN)
                    .build();

            default -> throw new IllegalArgumentException("Unsupported role: " + request.getRole());
        }

        userRepository.save(user); // only one save outside the switch

        String token = jwtService.generateToken(
                Map.of("role", user.getRole().name()),
                user.getEmail()
        );

        return new AuthResponse(token);
    }
}
