package com.amn.entity;

import com.amn.entity.enums.FileType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity

public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // For example: "Analysis"
    private String description;
    private String url;
    private LocalDate uploadDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;

    @ManyToOne
    @JoinColumn(name = "medical_folder_id")
    @JsonBackReference
    @JsonIgnore// Prevents infinite loop
    private MedicalFolder medicalFolder;
}
