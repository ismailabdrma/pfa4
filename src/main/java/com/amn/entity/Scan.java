package com.amn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Scan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // For example: "Scan"
    private String description;
    private String url;
    private LocalDateTime uploadDate;


    @ManyToOne
    @JoinColumn(name = "medical_folder_id")
    private MedicalFolder medicalFolder;
}
