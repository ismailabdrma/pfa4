package com.amn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIgnore
    private Patient patient;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<VisitLog> visitLogs;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Scan> scans;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Analysis> analyses;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Surgery> surgeries;

    @OneToMany(mappedBy = "medicalFolder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Prescription> prescriptions = new ArrayList<>();
}
