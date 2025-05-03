package com.amn.entity;

import com.amn.entity.enums.PrescriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicationName;
    private String dosage;
    private String period;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;

    private boolean permanent;

    private LocalDateTime prescribedDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "pharmacist_id")
    private Pharmacist dispensingPharmacist;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor prescribingDoctor;
}
