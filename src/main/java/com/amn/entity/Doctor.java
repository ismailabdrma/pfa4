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
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)

public class Doctor extends User {
    private String matricule;
    private String specialty;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    // Relationship with Admin (who approved this doctor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Admin approvedBy;

    @OneToMany(mappedBy = "doctor",fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<MedicalRecord> createdRecords;
    @OneToMany(mappedBy = "prescribingDoctor", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    private List<Prescription> prescriptions;


}