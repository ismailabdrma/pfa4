package com.amn.service;

import com.amn.entity.Medication;
import com.amn.entity.Patient;
import com.amn.entity.Prescription;
import com.amn.repository.MedicationRepository;
import com.amn.repository.PatientRepository;
import com.amn.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;

    // Create a new prescription
    public Prescription createPrescription(Long patientId, Prescription prescription) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        prescription.setPrescribedDate(LocalDateTime.now());
        prescription.setPatient(patient);

        return prescriptionRepository.save(prescription);
    }

    // Add medications to existing prescription
    public Prescription addMedications(Long prescriptionId, List<Medication> medications) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        for (Medication med : medications) {
            med.setPrescription(prescription);
            medicationRepository.save(med);
        }

        return prescription;
    }
}



