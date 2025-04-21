package com.amn.entity;


import com.amn.entity.enums.MedicationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String size; // e.g. "500mg"
    private double price;

    @Enumerated(EnumType.STRING)
    private MedicationType medicationType;

    @ManyToOne
    @JoinColumn(name = "added_by_pharmacist_id")
    private Pharmacist addedBy;
}
