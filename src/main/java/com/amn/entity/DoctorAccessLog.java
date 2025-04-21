package com.amn.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class DoctorAccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Patient patient;

    private LocalDateTime accessTime;
    private String purpose;
}

