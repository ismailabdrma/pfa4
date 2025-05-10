package com.amn.dto;

import com.amn.entity.Prescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {

    private Long id;
    private String status;
    private boolean permanent;
    private LocalDateTime prescribedDate;
    private LocalDateTime dispensedDate;
    private String prescribingDoctorName;
    private String dispensingPharmacistName;

    // ✅ New Fields
    private Long medicalRecordId;
    private Long medicalFolderId;
    private Long patientId;
    private String patientName;
    private String patientCIN;

    private List<MedicationDTO> medications;

    /**
     * ✅ Static Method for Mapping from Entity to DTO
     */
    public static PrescriptionDTO fromEntity(Prescription prescription) {
        return PrescriptionDTO.builder()
                .id(prescription.getId())
                .status(prescription.getStatus() != null ? prescription.getStatus().name() : "UNKNOWN")
                .permanent(prescription.isPermanent())
                .prescribedDate(prescription.getPrescribedDate())
                .dispensedDate(prescription.getDispensedDate())

                .prescribingDoctorName(
                        prescription.getPrescribingDoctor() != null
                                ? prescription.getPrescribingDoctor().getFullName()
                                : "Docteur inconnu"
                )
                .dispensingPharmacistName(
                        prescription.getDispensingPharmacist() != null
                                ? prescription.getDispensingPharmacist().getFullName()
                                : "Pharmacien inconnu"
                )

                // ✅ Link to Medical Record
                .medicalRecordId(
                        prescription.getMedicalRecord() != null
                                ? prescription.getMedicalRecord().getId()
                                : null
                )

                // ✅ Link to Medical Folder
                .medicalFolderId(
                        prescription.getMedicalFolder() != null
                                ? prescription.getMedicalFolder().getId()
                                : null
                )

                // ✅ Patient Details
                .patientId(
                        prescription.getPatient() != null
                                ? prescription.getPatient().getId()
                                : null
                )
                .patientName(
                        prescription.getPatient() != null
                                ? prescription.getPatient().getFullName()
                                : "Inconnu"
                )
                .patientCIN(
                        prescription.getPatient() != null
                                ? prescription.getPatient().getCin()
                                : "Inconnu"
                )

                // ✅ Medications Mapping
                .medications(
                        prescription.getMedications() != null
                                ? prescription.getMedications().stream()
                                .map(MedicationDTO::fromEntity)
                                .collect(Collectors.toList())
                                : List.of()
                )

                .build();
    }
}
