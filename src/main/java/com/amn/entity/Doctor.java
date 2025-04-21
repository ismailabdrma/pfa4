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
public class Doctor extends User {
    private String matricule;
    private String specialty;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    // Relationship with Admin (who approved this doctor)
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Admin approvedBy;

    @OneToMany(mappedBy = "doctor")
    private List<MedicalRecord> createdRecords;
    @OneToMany(mappedBy = "prescribingDoctor", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;

}