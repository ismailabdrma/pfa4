package com.amn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vaccineName;
    private int doseNumber;
    private String manufacturer;
    private LocalDate vaccinationDate;

    @ManyToOne
    @JoinColumn(name = "medical_folder_id")
    private MedicalFolder medicalFolder;
}
