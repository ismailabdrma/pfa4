package com.amn.service;

import com.amn.dto.MedicationDTO;
import com.amn.entity.Medication;
import com.amn.entity.Pharmacist;
import com.amn.entity.enums.Role;
import com.amn.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    // List all medications - role-based filtering
    public List<MedicationDTO> getAllMedications(Role userRole) {
        return medicationRepository.findAll().stream()
                .map(med -> mapToDTO(med, userRole))
                .collect(Collectors.toList());
    }

    // Search medication by name (optional filtering)
    public List<MedicationDTO> searchByName(String keyword, Role userRole) {
        return medicationRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(med -> mapToDTO(med, userRole))
                .collect(Collectors.toList());
    }

    // Get single medication by ID (optional access control)
    public Optional<MedicationDTO> getById(Long id, Role userRole) {
        return medicationRepository.findById(id)
                .map(med -> mapToDTO(med, userRole));
    }

    // Add a new medication - pharmacists only
    public Medication addMedication(Medication medication, Pharmacist pharmacist) {
        medication.setAddedBy(pharmacist);
        return medicationRepository.save(medication);
    }

    // Internal DTO mapping (filtering price if not pharmacist)
    private MedicationDTO mapToDTO(Medication med, Role role) {
        MedicationDTO dto = new MedicationDTO();
        dto.setId(med.getId());
        dto.setName(med.getName());
        dto.setSize(med.getSize());
        dto.setType(med.getMedicationType());
        if (role == Role.PHARMACIST) {
            dto.setPrice(med.getPrice());
        }
        return dto;
    }

}