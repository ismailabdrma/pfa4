package com.amn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

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
    @JsonIgnore
    private Patient patient;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<VisitLog> visitLogs;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Scan> scans;

    @OneToMany(mappedBy = "medicalFolder", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Analysis> analyses;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Surgery> surgeries;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Prescription> prescriptions = new ArrayList<>();
}
