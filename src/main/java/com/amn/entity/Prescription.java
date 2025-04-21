package com.amn.entity;

import com.amn.entity.enums.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription extends MedicalDocument {
    private String medicationName;
    private String dosage;
    private String period;


    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;
    private boolean permanent;

    // Relationships remain the same
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "pharmacist_id")
    private Pharmacist dispensingPharmacist;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor prescribingDoctor;
}