package com.amn.dto;

import com.amn.entity.enums.Role;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role; // Fixed: was 'S' instead of 'String'

    // PATIENT-specific fields
    private String cin;
    private LocalDate birthDate;
    private String bloodType;
    private String chronicDiseases;
    private String allergies;
    private String emergencyContact;
    private Boolean hasHeartProblem;
    private Boolean hasSurgery;
    private String matricule;




    // DOCTOR-specific (optional)
    private String specialization;
}

