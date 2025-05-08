package com.amn.controller;

import com.amn.dto.AuthRequest;
import com.amn.dto.AuthResponse;
import com.amn.dto.RegisterRequest;
import com.amn.entity.*;
import com.amn.entity.enums.AccountStatus;
import com.amn.entity.enums.OTPStatus;
import com.amn.entity.enums.Role;
import com.amn.repository.OTPRepository;
import com.amn.repository.UserRepository;
import com.amn.security.JwtService;
import com.amn.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final OTPRepository otpRepository;

    /**
     * ✅ LOGIN ENDPOINT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
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

        // ADMIN - Immediate Token Generation
        if (user.getRole() == Role.ADMIN) {
            String token = jwtService.generateToken(Map.of("role", user.getRole().name()), user.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, "ADMIN"));
        }

        // DOCTOR or PHARMACIST - OTP Verification Required
        if (user.getRole() == Role.DOCTOR || user.getRole() == Role.PHARMACIST) {
            AccountStatus status = (user instanceof Doctor doctor) ? doctor.getStatus() : ((Pharmacist) user).getStatus();

            if (status != AccountStatus.APPROVED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your account is not approved by the admin.");
            }

            otpService.sendOtpToUser(user);

            return ResponseEntity.ok(Map.of(
                    "message", "OTP sent to your email. Please verify.",
                    "role", user.getRole().name(),
                    "status", "APPROVED"
            ));
        }

        // PATIENT - OTP Verification Required
        otpService.sendOtpToUser(user);
        return ResponseEntity.ok(Map.of(
                "message", "OTP sent to your email. Please verify.",
                "role", "PATIENT",
                "status", "PENDING"
        ));
    }

    /**
     * ✅ VERIFY OTP ENDPOINT
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestParam String email, @RequestParam String otpCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        OTP otp = otpRepository.findTopByUserIdOrderByExpirationDesc(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not found"));

        if (!otp.getCode().equals(otpCode) || otp.getExpiration().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        // Mark OTP as verified
        otp.setStatus(OTPStatus.VERIFIED);
        otpRepository.save(otp);

        // Generate and return token
        String token = jwtService.generateToken(Map.of("role", user.getRole().name()), user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
    }

    /**
     * ✅ REGISTER ENDPOINT
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        Role role = request.getRole();
        User user;

        switch (role) {
            case DOCTOR -> user = Doctor.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .matricule(request.getMatricule())
                    .specialty(request.getSpecialization())
                    .status(AccountStatus.PENDING)
                    .role(Role.DOCTOR)
                    .build();

            case PHARMACIST -> user = Pharmacist.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .matricule(request.getMatricule())
                    .status(AccountStatus.PENDING)
                    .role(Role.PHARMACIST)
                    .build();

            case PATIENT -> user = Patient.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .cin(request.getCin())
                    .birthDate(request.getBirthDate())
                    .bloodType(request.getBloodType())
                    .emergencyContact(request.getEmergencyContact())
                    .allergies(request.getAllergies())
                    .chronicDiseases(request.getChronicDiseases())
                    .hasHeartProblem(request.getHasHeartProblem())
                    .hasSurgery(request.getHasSurgery())
                    .role(Role.PATIENT)
                    .build();

            case ADMIN -> user = Admin.builder()
                    .fullName(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .role(Role.ADMIN)
                    .build();

            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role");
        }

        userRepository.save(user);

        // Generate token immediately upon registration
        String token = jwtService.generateToken(Map.of("role", user.getRole().name()), user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
    }
}
