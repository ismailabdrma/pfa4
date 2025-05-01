package com.amn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.amn.entity.enums.Role;

import lombok.experimental.SuperBuilder;

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
    private MedicalFolder medicalFolder;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin managedBy;
    public Patient(String name, String email, String password) {
        super(name, email, password, Role.PATIENT);
    }


}
