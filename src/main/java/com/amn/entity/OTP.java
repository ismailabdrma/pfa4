package com.amn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private LocalDateTime expiration;
    private Boolean used;

    // Relationship with Patient
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Relationship with Doctor who generated this OTP
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor generatedBy;
}