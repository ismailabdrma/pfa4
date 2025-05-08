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
    private Long medicalFolderId;

    // Use raw entity lists — NO DTOs
    private List<MedicalRecordDTO> medicalRecords;

    private List<Vaccination> vaccinations;
    private List<VisitLog> visitLogs;
    private List<ScanDTO> scans;
    private List<SurgeryDTO> surgeries;
    private List<PrescriptionDTO> prescriptions;
    // ✅ Fix here


    private List<AnalysisDTO> analyses;
}
