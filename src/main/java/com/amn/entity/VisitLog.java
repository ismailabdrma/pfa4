package com.amn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_folder_id")
    @JsonIgnore
    @BatchSize(size = 10)
    private MedicalFolder medicalFolder;
}
