package com.amn.service;

import com.amn.dto.PrescriptionDTO;
import com.amn.entity.Prescription;
import com.amn.entity.enums.PrescriptionStatus;
import com.amn.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacistService {

    private final PrescriptionRepository prescriptionRepository;

    /**
     * ✅ Get prescriptions for a patient by CIN and Full Name.
     * This returns all prescriptions regardless of status.
     */
    public List<PrescriptionDTO> getPrescriptionsByCinAndName(String cin, String fullName) {
        return prescriptionRepository.findAll().stream()
                .filter(p -> p.getPatient().getCin().equalsIgnoreCase(cin)
                        && p.getPatient().getFullName().equalsIgnoreCase(fullName))
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Get only DISPENSED prescriptions for a patient by CIN and Full Name.
     */
    public List<PrescriptionDTO> getDispensedPrescriptionsByCinAndName(String cin, String fullName) {
        return prescriptionRepository.findAll().stream()
                .filter(p -> p.getPatient().getCin().equalsIgnoreCase(cin)
                        && p.getPatient().getFullName().equalsIgnoreCase(fullName)
                        && p.getStatus() == PrescriptionStatus.DISPENSED)
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
    public PrescriptionDTO getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        return PrescriptionDTO.fromEntity(prescription);
    }
}
