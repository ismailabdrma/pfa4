package com.amn.service;

import com.amn.dto.PrescriptionDTO;
import com.amn.entity.Prescription;
import com.amn.entity.enums.PrescriptionStatus;
import com.amn.repository.PrescriptionRepository;
import com.amn.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PharmacistService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;

    /**
     * ✅ Get ALL prescriptions for a patient by CIN and Full Name regardless of status.
     */
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getPrescriptionsByCinAndName(String cin, String fullName) {
        // Find all prescriptions for this patient
        List<Prescription> prescriptions = prescriptionRepository.findByPatientCinAndName(cin, fullName);

        // Log for debugging
        System.out.println("Found " + prescriptions.size() + " prescriptions for patient with CIN: " + cin);

        return prescriptions.stream()
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Get only PENDING prescriptions for a patient by CIN and Full Name.
     * This is what pharmacists need to see to dispense medications.
     */
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getPendingPrescriptionsByCinAndName(String cin, String fullName) {
        List<Prescription> prescriptions = prescriptionRepository.findByPatientCinAndNameAndStatus(
                cin, fullName, PrescriptionStatus.PENDING);

        System.out.println("Found " + prescriptions.size() + " PENDING prescriptions for patient with CIN: " + cin);

        return prescriptions.stream()
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Get only DISPENSED prescriptions for a patient by CIN and Full Name.
     */
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getDispensedPrescriptionsByCinAndName(String cin, String fullName) {
        List<Prescription> prescriptions = prescriptionRepository.findByPatientCinAndNameAndStatus(
                cin, fullName, PrescriptionStatus.DISPENSED);

        return prescriptions.stream()
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Mark a prescription as DISPENSED.
     */
    public PrescriptionDTO markAsDispensed(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        prescription.setStatus(PrescriptionStatus.DISPENSED);
        prescription.setDispensedDate(java.time.LocalDateTime.now());
        Prescription updated = prescriptionRepository.save(prescription);

        return PrescriptionDTO.fromEntity(updated);
    }

    /**
     * ✅ Get a single prescription by ID.
     */
    @Transactional(readOnly = true)
    public PrescriptionDTO getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        return PrescriptionDTO.fromEntity(prescription);
    }
}