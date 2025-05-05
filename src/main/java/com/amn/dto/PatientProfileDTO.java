package com.amn.dto;

import com.amn.entity.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileDTO {
    private Long id;
    private String fullName;
    private String cin;
    private String bloodType;
    private String birthDate;
    private String emergencyContact;
    private String allergies;
    private String chronicDiseases;
    private boolean hasHeartProblem;
    private boolean hasSurgery;
    private String email;
    private String phone;
    private String address;

    private Long medicalFolderId; // ✅ Add this line

    // Medical folder contents
    private List<MedicalRecord> medicalRecords;
    private List<Vaccination> vaccinations;
    private List<VisitLog> visitLogs;
    private List<Scan> scans;
    private List<Surgery> surgeries;
    private List<Prescription> prescriptions;
    private List<Analysis> analyses;
}
