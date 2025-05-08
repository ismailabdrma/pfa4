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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

import static com.amn.entity.enums.Role.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MedicalFolderRepository medicalFolderRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    public AuthResponse register(RegisterRequest request) {
        User user;

        switch (request.getRole()) {
            case DOCTOR -> {
                if (request.getMatricule() == null || request.getSpecialization() == null) {
                    throw new IllegalArgumentException("Matricule and Specialization are required for Doctors.");
                }

                user = Doctor.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .matricule(request.getMatricule())
                        .specialty(request.getSpecialization())
                        .status(AccountStatus.PENDING)  // Must be approved by admin
                        .role(DOCTOR)
                        .build();
            }

            case PHARMACIST -> {
                if (request.getMatricule() == null) {
                    throw new IllegalArgumentException("Matricule is required for Pharmacists.");
                }

                user = Pharmacist.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .matricule(request.getMatricule())
                        .status(AccountStatus.PENDING)  // Must be approved by admin
                        .role(PHARMACIST)
                        .build();
            }

            case ADMIN -> {
                user = Admin.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .role(ADMIN)
                        .build();
            }

            case PATIENT -> {
                Patient patient = Patient.builder()
                        .fullName(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .role(PATIENT)
                        .cin(request.getCin())
                        .birthDate(request.getBirthDate())
                        .bloodType(request.getBloodType())
                        .chronicDiseases(request.getChronicDiseases())
                        .allergies(request.getAllergies())
                        .emergencyContact(request.getEmergencyContact())
                        .hasHeartProblem(request.getHasHeartProblem())
                        .hasSurgery(request.getHasSurgery())// Patient is directly active
                        .build();

                patient = patientRepository.save(patient);

                // Create a new MedicalFolder for the patient
                MedicalFolder folder = new MedicalFolder();
                folder.setPatient(patient);
                folder.setCreationDate(LocalDate.now());
                folder.setDescription("Dossier médical de " + patient.getFullName());
                medicalFolderRepository.save(folder);

                user = patient;
            }

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
