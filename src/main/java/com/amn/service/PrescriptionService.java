package com.amn.service;

import com.amn.dto.MedicationDTO;
import com.amn.dto.PrescriptionDTO;
import com.amn.entity.Prescription;
import com.amn.entity.Medication;
import com.amn.entity.enums.Role;
import com.amn.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public List<PrescriptionDTO> getPrescriptionsByPatient(Long patientId, Role role) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        return prescriptionRepository.findByPatient(patientId).stream()
                .map(prescription -> mapToDTO(prescription, role))
                .collect(Collectors.toList());
    }

    private PrescriptionDTO mapToDTO(Prescription prescription, Role role) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setPrescribedDate(prescription.getDate());

        // Map list of Medication to MedicationDTO
        List<MedicationDTO> meds = prescription.getMedications().stream()
                .map(med -> {
                    MedicationDTO mDto = new MedicationDTO();
                    mDto.setId(med.getId());
                    mDto.setName(med.getName());
                    mDto.setSize(med.getSize());
                    mDto.setType(med.getMedicationType());
                    if (role == Role.PHARMACIST) {
                        mDto.setPrice(med.getPrice());
                    }
                    return mDto;
                })
                .collect(Collectors.toList());
        dto.setMedications(meds);

        // Total price (only for pharmacists)
        if (role == Role.PHARMACIST) {
            double total = prescription.getMedications().stream()
                    .mapToDouble(Medication::getPrice)
                    .sum();
            dto.setTotalPrice(total);
        }

        // Doctor & Patient info
        dto.setDoctorName(prescription.getPrescribingDoctor().getFullName());
        dto.setPatientFullName(prescription.getPatient().getFullName());

        return dto;
    }



}
