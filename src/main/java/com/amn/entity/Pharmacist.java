package com.amn.entity;

import com.amn.entity.enums.AccountStatus;
import com.amn.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)

public class Pharmacist extends User {

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    // Relationship with Admin (who approved this pharmacist)
    @ManyToOne
    @JoinColumn(name = "approved_by_admin_id")
    private Admin approvedBy;
    private String matricule;

    // Relationship with Prescription (pharmacist dispenses prescriptions)
    @OneToMany(mappedBy = "dispensingPharmacist", cascade = CascadeType.ALL)
    private List<Prescription> dispensedPrescriptions;
    public Pharmacist(String name, String email, String password) {
        super(name, email, password, Role.PHARMACIST);
    }

}