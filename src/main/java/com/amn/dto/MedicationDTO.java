package com.amn.dto;

import com.amn.entity.Medication;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDTO {
    private String name;
    private String dosage;
    private String instructions;
    private double price;

    public static MedicationDTO fromEntity(Medication med) {
        return MedicationDTO.builder()
                .name(med.getName())
                .dosage(med.getDosage())
                .instructions(med.getInstructions())
                .price(med.getPrice())
                .build();
    }
}
