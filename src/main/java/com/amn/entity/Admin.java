package com.amn.entity;

import com.amn.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    // Relationship with Patient (1:many)
    @JsonIgnore

    @OneToMany(mappedBy = "managedBy", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private Set<Patient> managedPatients;

    // Relationships with Doctor (1:many)
    @JsonIgnore
    @OneToMany(mappedBy = "approvedBy", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private Set<Doctor> approvedDoctors;

    // Relationships with Pharmacist (1:many)
    @JsonIgnore
    @OneToMany(mappedBy = "approvedBy", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private Set<Pharmacist> approvedPharmacists;


}