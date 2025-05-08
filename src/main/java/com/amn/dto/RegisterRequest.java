package com.amn.dto;

import com.amn.entity.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RegisterRequest {

    // Common Fields
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;

    // PATIENT-specific fields
    private String cin;
    private LocalDate birthDate;
    private String bloodType;
    private String chronicDiseases;
    private String allergies;
    private String emergencyContact;
    private Boolean hasHeartProblem = Boolean.FALSE;
    private Boolean hasSurgery = Boolean.FALSE;

    // DOCTOR & PHARMACIST - Common Fields
    private String matricule;

    // DOCTOR-specific field
    private String specialization;
}
