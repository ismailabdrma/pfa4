package com.amn.entity;

import com.amn.entity.enums.MedicationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class
Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String instructions;

    private String name;

    private String dosage;

    private String period;

    private boolean permanent;

    private String size;
    private double Price= 0.0;// ✅ Required for getSize()

    @Enumerated(EnumType.STRING)
    private MedicationType type; // ✅ Required for OTC vs PRESCRIPTION

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    @BatchSize(size = 10)
    @JsonIgnore
    private Prescription prescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private Pharmacist addedBy; // ✅ Required for setAddedBy()

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private Patient patient; // ✅ Required for setPatient()
}
