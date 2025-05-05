package com.amn.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDTO {
    private Long id;
    private String vaccineName;
    private int doseNumber;
    private String manufacturer;
    private LocalDate vaccinationDate;
}