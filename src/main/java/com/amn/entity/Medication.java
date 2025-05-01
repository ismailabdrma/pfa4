package com.amn.entity;


import com.amn.entity.enums.MedicationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String size; // e.g. "500mg"
    @Getter
    private double price;

    @Enumerated(EnumType.STRING)
    private MedicationType Type;

    @ManyToOne
    @JoinColumn(name = "added_by_pharmacist_id")
    private Pharmacist addedBy;
    @ManyToOne
    private Prescription prescription;


}