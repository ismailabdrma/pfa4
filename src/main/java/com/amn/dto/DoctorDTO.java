package com.amn.dto;

import com.amn.entity.Doctor;
import com.amn.entity.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private String fullName;
    private String email;
    private String matricule;
    private String specialty;
    private AccountStatus status;
    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.fullName = doctor.getFullName();
        this.email = doctor.getEmail();
        this.matricule = doctor.getMatricule();
        this.specialty = doctor.getSpecialty();
}}
