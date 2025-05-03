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

    private String fullName;
    private String cin;
    private String bloodType;
    private String birthDate;
    private String emergencyContact;
    private String allergies;
    private String chronicDiseases;
    private boolean hasHeartProblem;
    private boolean hasSurgery;

    // Medical folder contents
    private List<MedicalRecord> medicalRecords; // Appointments = visits
    private List<Vaccination> vaccinations;
    private List<VisitLog> visitLogs;
    private List<Scan> scans;
    private List<Surgery> surgeries;
}
