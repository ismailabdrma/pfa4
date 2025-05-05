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
public class Surgery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // For example: "Surgery"
    private String description;
    private String url;
    private LocalDate uploadDate;
    private String surgeonName;
    private String hospitalName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_folder_id")

    @JsonIgnore
    private MedicalFolder medicalFolder;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;
}

