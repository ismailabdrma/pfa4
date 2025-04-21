package com.amn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin extends User {

    // Relationship with Patient (1:many)
    @OneToMany(mappedBy = "managedBy", cascade = CascadeType.ALL)
    private Set<Patient> managedPatients;

    // Relationships with Doctor (1:many)
    @OneToMany(mappedBy = "approvedBy", cascade = CascadeType.ALL)
    private Set<Doctor> approvedDoctors;

    // Relationships with Pharmacist (1:many)
    @OneToMany(mappedBy = "approvedBy", cascade = CascadeType.ALL)
    private Set<Pharmacist> approvedPharmacists;
}