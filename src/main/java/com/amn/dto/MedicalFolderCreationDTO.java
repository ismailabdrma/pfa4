// ✅ File: MedicalFolderCreationDTO.java
package com.amn.dto;

import lombok.Data;

@Data
public class MedicalFolderCreationDTO {
    private String cin;
    private String fullName;

    // Optional patient info to update
    private String bloodType;
    private String emergencyContact;
    private String allergies;
    private String chronicDiseases;
    private boolean hasHeartProblem;
    private boolean hasSurgery;
    private String birthDate; // formatted as yyyy-MM-dd
}
