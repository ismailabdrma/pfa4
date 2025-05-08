package com.amn.service;

import com.amn.dto.*;
import com.amn.entity.Patient;
import com.amn.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientProfileDTO getPatientProfileByCin(String cin) {
        Patient patient = patientRepository.findByCin(cin)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return PatientProfileDTO.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .cin(patient.getCin())
                .bloodType(patient.getBloodType())
                .birthDate(String.valueOf(patient.getBirthDate()))
                .emergencyContact(patient.getEmergencyContact())
                .allergies(patient.getAllergies())
                .chronicDiseases(patient.getChronicDiseases())
                .hasHeartProblem(patient.isHasHeartProblem())
                .hasSurgery(patient.isHasSurgery())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .medicalFolderId(patient.getMedicalFolder() != null ? patient.getMedicalFolder().getId() : null)
                .medicalRecords(patient.getMedicalFolder() != null ?
                        patient.getMedicalFolder().getMedicalRecords().stream()
                                .map(MedicalRecordDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .prescriptions(patient.getMedicalFolder() != null ?
                        patient.getMedicalFolder().getPrescriptions().stream()
                                .map(PrescriptionDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .scans(patient.getMedicalFolder() != null ?
                        patient.getMedicalFolder().getScans().stream()
                                .map(ScanDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .analyses(patient.getMedicalFolder() != null ?
                        patient.getMedicalFolder().getAnalyses().stream()
                                .map(AnalysisDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .surgeries(patient.getMedicalFolder() != null ?
                        patient.getMedicalFolder().getSurgeries().stream()
                                .map(SurgeryDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .build();
    }
}
