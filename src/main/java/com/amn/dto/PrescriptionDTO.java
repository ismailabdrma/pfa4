package com.amn.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PrescriptionDTO {

    private Long id;
    private LocalDate prescribedDate;
    private List<MedicationDTO> medications;
    private double totalPrice;
    private String doctorName;
    private String patientFullName;


    // Only shown to pharmacists
}
