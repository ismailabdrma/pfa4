package com.amn.entity;

import com.amn.entity.enums.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
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
    private LocalDateTime prescribedDate; // fixed

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "pharmacist_id")
    private Pharmacist dispensingPharmacist;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor prescribingDoctor;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id")
    private List<Medication> medications;
}
