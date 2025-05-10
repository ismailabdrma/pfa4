package com.amn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Setter
@Getter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String reason;
    private String diagnosis;
    private String notes;
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    @BatchSize(size = 10)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @BatchSize(size = 10)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_folder_id")
    @JsonIgnore//
    @BatchSize(size = 10)// fixed here
    private MedicalFolder medicalFolder;
    // ✅ Add this relationship
    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)

    private List<Prescription> prescriptions;
}
