package com.amn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class MedicalFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate creationDate;
    private String description;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    private List<VisitLog> visitLogs;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    private List<Scan> scans;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    private List<Analysis> analyses;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Surgery> surgeries;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();
}
