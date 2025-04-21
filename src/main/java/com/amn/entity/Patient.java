package com.amn.entity;

import com.amn.entity.enums.BloodType;
import com.amn.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String cin;
    @JsonIgnore

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String password;
    private String email;
    private String phone;
    private String address;
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    private String emergencyContact;
    private String allergies;
    private String chronicDiseases;
    private boolean hasHeartProblem;
    private boolean hasSurgery;
    private Date birthDate; // This was in your UML but missing in your code

    // Relationship with Admin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managed_by_admin_id")
    private Admin managedBy;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;



    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;

    // From your UML, there should also be a relationship with OTP
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<OTP> otps;
    @OneToMany(mappedBy = "patient")
    private List<MedicalRecord> medicalRecords;

}