package com.amn.service;

import com.amn.dto.AuthResponse;
import com.amn.dto.RegisterRequest;
import com.amn.entity.*;
import com.amn.entity.enums.AccountStatus;
import com.amn.entity.enums.Role;
import com.amn.repository.MedicalFolderRepository;
import com.amn.repository.PatientRepository;
import com.amn.repository.UserRepository;
import com.amn.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MedicalFolderRepository medicalFolderRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        if (request.getRole() == null) {
            throw new IllegalArgumentException("Role is required.");
        }

        User user;

        switch (request.getRole()) {
            case DOCTOR -> {
                if (request.getMatricule() == null || request.getMatricule().isBlank()) {
                    throw new IllegalArgumentException("Matricule is required for Doctors.");
                }
                if (request.getSpecialization() == null || request.getSpecialization().isBlank()) {
                    throw new IllegalArgumentException("Specialization is required for Doctors.");
                }

                user = Doctor.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .matricule(request.getMatricule())
                        .specialty(request.getSpecialization())
                        .status(AccountStatus.PENDING)
                        .role(Role.DOCTOR)
                        .build();
            }

            case PHARMACIST -> {
                if (request.getMatricule() == null || request.getMatricule().isBlank()) {
                    throw new IllegalArgumentException("Matricule is required for Pharmacists.");
                }

                user = Pharmacist.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .matricule(request.getMatricule())
                        .status(AccountStatus.PENDING)
                        .role(Role.PHARMACIST)
                        .build();
            }

            case ADMIN -> {
                user = Admin.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .role(Role.ADMIN)
                        .build();
            }

            case PATIENT -> {
                Patient patient = Patient.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .cin(request.getCin())
                        .birthDate(request.getBirthDate())
                        .bloodType(request.getBloodType())
                        .chronicDiseases(request.getChronicDiseases())
                        .allergies(request.getAllergies())
                        .emergencyContact(request.getEmergencyContact())
                        .hasHeartProblem(request.getHasHeartProblem() != null ? request.getHasHeartProblem() : Boolean.FALSE)
                        .hasSurgery(request.getHasSurgery() != null ? request.getHasSurgery() : Boolean.FALSE)
                        .role(Role.PATIENT)
                        .build();

                patient = patientRepository.save(patient);

                // Create Medical Folder for Patient
                MedicalFolder folder = new MedicalFolder();
                folder.setPatient(patient);
                folder.setCreationDate(LocalDate.now());
                folder.setDescription("Medical Folder for " + patient.getFullName());
                medicalFolderRepository.save(folder);

                user = patient;
            }

            default -> throw new IllegalArgumentException("Unsupported role: " + request.getRole());
        }

        // Save User
        userRepository.save(user);

        // Generate JWT Token
        String token = jwtService.generateToken(
                Map.of("role", user.getRole().name()),
                user.getEmail()
        );

        return new AuthResponse(token);
    }
}
