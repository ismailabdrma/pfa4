package com.amn.dto;

import com.amn.entity.Medication;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDTO {
    private Long id; // ✅ Include ID for referencing
    private String name;
    private String dosage;
    private String period;
    private boolean permanent;
    private String instructions;
    private double price;

    /**
     * ✅ Converts a Medication entity to a MedicationDTO
     */
    public static MedicationDTO fromEntity(Medication med) {
        return MedicationDTO.builder()
                .id(med.getId())
                .name(med.getName())
                .dosage(med.getDosage())
                .period(med.getPeriod())
                .permanent(med.isPermanent())
                .instructions(med.getInstructions())
                .price(med.getPrice())
                .build();
    }
}
