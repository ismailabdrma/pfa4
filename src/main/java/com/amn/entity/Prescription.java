package com.amn.entity;

import com.amn.entity.enums.PrescriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime dispensedDate;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;

    @ManyToOne
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    private Pharmacist dispensingPharmacist;

    @ManyToOne
    private Doctor prescribingDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_folder_id")
    @JsonIgnore
    private MedicalFolder medicalFolder;
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Medication> medications = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "medical_record_id")
    @JsonIgnore
    @BatchSize(size = 10)
    private MedicalRecord medicalRecord;
// 🔥 THIS FIXES THE ERROR

}
