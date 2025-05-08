package com.amn.entity;

import com.amn.entity.enums.MedicationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medication {

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

    @ManyToOne
    @JsonIgnore
    private Prescription prescription;

    @ManyToOne
    @JsonIgnore
    private Pharmacist addedBy; // ✅ Required for setAddedBy()

    @ManyToOne
    @JsonIgnore
    private Patient patient; // ✅ Required for setPatient()
}
