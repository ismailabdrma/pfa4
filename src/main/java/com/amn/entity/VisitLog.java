package com.amn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime visitDate;
    private String doctorName;
    private String reason;
    private String actionsTaken; // What happened during the visit (treatment, diagnosis, advice)

    @ManyToOne
    @JoinColumn(name = "medical_folder_id")
    @JsonIgnore
    private MedicalFolder medicalFolder;
}
