package com.amn.entity;

import com.amn.entity.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pharmacist extends User {

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    // Relationship with Admin (who approved this pharmacist)
    @ManyToOne
    @JoinColumn(name = "approved_by_admin_id")
    private Admin approvedBy;

    // Relationship with Prescription (pharmacist dispenses prescriptions)
    @OneToMany(mappedBy = "dispensingPharmacist", cascade = CascadeType.ALL)
    private List<Prescription> dispensedPrescriptions;
}