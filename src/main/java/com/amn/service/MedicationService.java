package com.amn.service;

import com.amn.entity.Medication;
import com.amn.entity.Pharmacist;
import com.amn.entity.enums.MedicationType;
import com.amn.repository.MedicationRepository;
import com.amn.repository.PharmacistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final PharmacistRepository pharmacistRepository;

    // Add a new medication
    public Medication addMedication(Medication medication, Long pharmacistId) {
        Pharmacist pharmacist = pharmacistRepository.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        medication.setAddedBy(pharmacist);
        if (medication.getType() == null) {
            medication.setType(MedicationType.PRESCRIPTION); // Default if not set
        }
        return medicationRepository.save(medication);
    }

    // List all medications
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    // Search medications by name
    public List<Medication> searchMedicationsByName(String keyword) {
        return medicationRepository.findByNameContainingIgnoreCase(keyword);
    }
}
