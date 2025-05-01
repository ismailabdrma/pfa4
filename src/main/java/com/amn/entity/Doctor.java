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
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)

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
    public Doctor(String name, String email, String password) {
        super(name, email, password, Role.DOCTOR);
    }

}