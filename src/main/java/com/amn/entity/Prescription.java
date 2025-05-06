package com.amn.entity;

import com.amn.entity.enums.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicationName;
    private String dosage;
    private String period;
    private boolean permanent;

    private LocalDateTime prescribedDate;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Pharmacist dispensingPharmacist;

    @ManyToOne
    private Doctor prescribingDoctor;

    @ManyToOne
    @JoinColumn(name = "medical_folder_id")
    private MedicalFolder medicalFolder;
// 🔥 THIS FIXES THE ERROR

}
