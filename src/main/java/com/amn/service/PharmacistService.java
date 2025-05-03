package com.amn.service;

import com.amn.entity.Medication;
import com.amn.entity.Patient;
import com.amn.entity.Pharmacist;
import com.amn.entity.Prescription;
import com.amn.entity.enums.MedicationType;
import com.amn.entity.enums.PrescriptionStatus;
import com.amn.repository.MedicationRepository;
import com.amn.repository.PatientRepository;
import com.amn.repository.PharmacistRepository;
import com.amn.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacistService {

    private final PharmacistRepository pharmacistRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;

    // ✅ View all prescriptions
    public List<Prescription> viewPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // ✅ View prescriptions by patient
    public List<Prescription> viewPrescriptionsByPatient(String cin, String fullName) {
        return prescriptionRepository.findAll().stream()
                .filter(p -> p.getPatient().getCin().equalsIgnoreCase(cin)
                        && p.getPatient().getFullName().equalsIgnoreCase(fullName))
                .toList();
    }

    // ✅ Dispense prescription
    public Prescription dispensePrescription(Long prescriptionId, Long pharmacistId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        Pharmacist pharmacist = pharmacistRepository.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        prescription.setDispensingPharmacist(pharmacist);
        prescription.setStatus(PrescriptionStatus.DISPENSED);
        return prescriptionRepository.save(prescription);
    }

    // ✅ Add OTC medication and generate a prescription
    public Medication addOTCMedication(Medication medication, Long pharmacistId, Long patientId) {
        Pharmacist pharmacist = pharmacistRepository.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        medication.setType(MedicationType.OTC);
        medication.setAddedBy(pharmacist);
        medication.setPatient(patient);

        // Create a flat prescription (NOT a list)
        Prescription prescription = Prescription.builder()
                .patient(patient)
                .prescribedDate(LocalDateTime.now())
                .status(PrescriptionStatus.DISPENSED)
                .dispensingPharmacist(pharmacist)
                .medicationName(medication.getName())
                .dosage(medication.getSize()) // or any other dose format
                .period("OTC") // optional placeholder
                .permanent(false)
                .build();

        prescriptionRepository.save(prescription);
        return medicationRepository.save(medication);
    }

}
