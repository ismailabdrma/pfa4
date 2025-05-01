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
public class Surgery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // For example: "Surgery"
    private String description;
    private String url;
    private LocalDateTime uploadDate;
    private String surgeonName;
    private String hospitalName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_folder_id")
    private MedicalFolder medicalFolder;
}
