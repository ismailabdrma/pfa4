package com.amn.entity;

import com.amn.entity.enums.FileType;
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
public class Scan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // For example: "Scan"
    private String description;
    private String url;
    private LocalDate uploadDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_folder_id")
    @JsonIgnore
    private MedicalFolder medicalFolder;
}
