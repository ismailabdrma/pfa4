package com.amn.entity;

import com.amn.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
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
    public Admin(String name, String email, String password) {
        super(name, email, password, Role.ADMIN);
    }

}