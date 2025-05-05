package com.amn.controller;

import com.amn.dto.PatientProfileDTO;
import com.amn.entity.MedicalFolder;
import com.amn.entity.Patient;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;
    private final MedicalFolderRepository medicalFolderRepository;
    private final ScanRepository scanRepository;
    private final AnalysisRepository analysisRepository;
    private final SurgeryRepository surgeryRepository;
    private final VaccinationRepository vaccinationRepository;
    private final PrescriptionRepository prescriptionRepository;

    @GetMapping("/me")
    public ResponseEntity<PatientProfileDTO> getPatientProfile(Authentication auth) {
        String email = auth.getName(); // Extract email from JWT

        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        MedicalFolder folder = medicalFolderRepository.findByPatientId(patient.getId())
                .orElseThrow(() -> new RuntimeException("Medical folder not found"));

        PatientProfileDTO profile = PatientProfileDTO.builder()
                .fullName(patient.getFullName())
                .cin(patient.getCin())
                .birthDate(String.valueOf(patient.getBirthDate()))
                .bloodType(patient.getBloodType())
                .emergencyContact(patient.getEmergencyContact())
                .allergies(patient.getAllergies())
                .chronicDiseases(patient.getChronicDiseases())
                .hasHeartProblem(patient.isHasHeartProblem())
                .hasSurgery(patient.isHasSurgery())
                .medicalRecords(folder.getMedicalRecords())
                .visitLogs(folder.getVisitLogs())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .analyses(folder.getAnalyses())
                .phone(patient.getPhone())
                .analyses(analysisRepository.findAllByMedicalFolderId(folder.getId()))//
                .scans(scanRepository.findAllByMedicalFolderId(folder.getId()))
                .surgeries(surgeryRepository.findAllByMedicalFolderId(folder.getId()))
                .prescriptions(prescriptionRepository.findAllByPatientId(patient.getId()))//
                .vaccinations(vaccinationRepository.findAllByMedicalFolderId(folder.getId())) // ✅ fixed
// ...

                .build();

        return ResponseEntity.ok(profile);
    }
}
