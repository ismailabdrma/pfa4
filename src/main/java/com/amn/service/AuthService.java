package com.amn.service;

import com.amn.dto.AuthResponse;
import com.amn.dto.RegisterRequest;
import com.amn.entity.Admin;
import com.amn.entity.Doctor;
import com.amn.entity.Patient;
import com.amn.entity.Pharmacist;
import com.amn.entity.User;
import com.amn.entity.enums.Role;
import com.amn.repository.UserRepository;
import com.amn.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

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
                    .role(Role.DOCTOR)
                    .build();
            case PATIENT -> user = Patient.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.PATIENT)
                    .build();
            case PHARMACIST -> user = Pharmacist.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.PHARMACIST)
                    .build();
            case ADMIN -> user = Admin.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            default -> throw new IllegalArgumentException("Unsupported role: " + request.getRole());
        }

        userRepository.save(user);

        String token = jwtService.generateToken(
                Map.of("role", user.getRole().name()),
                user.getEmail()
        );

        return new AuthResponse(token);
    }
}
