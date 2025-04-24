package com.amn.dto;

import com.amn.entity.enums.MedicationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MedicationDTO {
    private Long id;
    private String name;
    private String size;
    private double price; // Optional: only filled for pharmacists
    private MedicationType type;
}
