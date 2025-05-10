package com.amn.entity;

import com.amn.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
public class Patient extends User {

    private String cin;
    private String bloodType;
    private String emergencyContact;
    private String allergies;
    private String chronicDiseases;
    private boolean hasHeartProblem;
    private boolean hasSurgery;
    private LocalDate birthDate;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private MedicalFolder medicalFolder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    @BatchSize(size = 10)
    private Admin managedBy;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Medication> medications;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY)

    private List<OTP> otps;

}
