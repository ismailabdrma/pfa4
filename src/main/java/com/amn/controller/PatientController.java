package com.amn.controller;

import com.amn.dto.*;
import com.amn.entity.MedicalFolder;
import com.amn.entity.Patient;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        String email = auth.getName();

        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        MedicalFolder folder = medicalFolderRepository.findByPatientId(patient.getId())
                .orElseThrow(() -> new RuntimeException("Medical folder not found"));

        // ✅ Convert to DTOs
        var scanDTOs = scanRepository.findAllByMedicalFolderId(folder.getId())
                .stream().map(ScanDTO::fromEntity).collect(Collectors.toList());

        var analysisDTOs = analysisRepository.findAllByMedicalFolderId(folder.getId())
                .stream().map(AnalysisDTO::fromEntity).collect(Collectors.toList());

        var surgeryDTOs = surgeryRepository.findAllByMedicalFolderId(folder.getId())
                .stream().map(SurgeryDTO::fromEntity).collect(Collectors.toList());
        List<PrescriptionDTO> prescriptions = prescriptionRepository.findAllByPatientId(patient.getId())
                .stream()
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());


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
                .medicalRecords(
                folder.getMedicalRecords().stream()
                        .map(MedicalRecordDTO::fromEntity)
                        .collect(Collectors.toList())
        )

                .visitLogs(folder.getVisitLogs())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .analyses(analysisDTOs)
                .scans(scanDTOs)
                .surgeries(surgeryDTOs)
                .prescriptions(prescriptions) // Now it's List<PrescriptionDTO>
                .vaccinations(vaccinationRepository.findAllByMedicalFolderId(folder.getId()))
                .build();

        return ResponseEntity.ok(profile);
    }}
