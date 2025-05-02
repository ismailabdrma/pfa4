package com.amn.service;

import com.amn.entity.Medication;
import com.amn.entity.Pharmacist;
import com.amn.entity.Prescription;
import com.amn.entity.enums.MedicationType;
import com.amn.entity.enums.PrescriptionStatus;
import com.amn.repository.MedicationRepository;
import com.amn.repository.PharmacistRepository;
import com.amn.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PharmacistService {

    private final PharmacistRepository pharmacistRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
   // private final PasswordEncoder passwordEncoder;

    // Pharmacist login
   // public Optional<Pharmacist> login(String email, String password) {
        //ptional<Pharmacist> pharmacistOpt = pharmacistRepository.findByEmail(email);
       // if (pharmacistOpt.isPresent() && passwordEncoder.matches(password, pharmacistOpt.get().getPassword())) {
        //    return pharmacistOpt;
        //}
        //return Optional.empty();
    //}

    // View prescriptions to dispense
    public List<Prescription> viewPrescriptions() {
        return prescriptionRepository.findAll(); // Can be filtered later if needed
    }

    // Dispense prescription
    public Prescription dispensePrescription(Long prescriptionId, Long pharmacistId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        Pharmacist pharmacist = pharmacistRepository.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        prescription.setDispensingPharmacist(pharmacist);
        prescription.setStatus(PrescriptionStatus.DISPENSED);
        // Mark as dispensed
        return prescriptionRepository.save(prescription);
    }

    // Add Over The Counter Medication
    public Medication addOTCMedication(Medication medication, Long pharmacistId) {
        Pharmacist pharmacist = pharmacistRepository.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        medication.setType(MedicationType.OTC);
        medication.setAddedBy(pharmacist);

        return medicationRepository.save(medication);
    }
    public List<Prescription> viewPrescriptionsByPatient(String cin, String fullName) {
        return prescriptionRepository.findAll().stream()
                .filter(p -> p.getPatient().getCin().equalsIgnoreCase(cin)
                        && p.getPatient().getFullName().equalsIgnoreCase(fullName))
                .toList();
    }

}
