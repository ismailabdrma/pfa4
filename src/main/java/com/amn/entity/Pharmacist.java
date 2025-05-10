package com.amn.entity;

import com.amn.entity.enums.AccountStatus;
import com.amn.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_admin_id")
    @BatchSize(size = 10)
    @JsonIgnore
    private Admin approvedBy;
    private String matricule;

    // Relationship with Prescription (pharmacist dispenses prescriptions)
    @OneToMany(mappedBy = "dispensingPharmacist", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Prescription> dispensedPrescriptions;


}