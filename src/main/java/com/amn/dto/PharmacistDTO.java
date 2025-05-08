package com.amn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacistDTO {
    private Long id;
    private String fullName;
    private String email;
    private String matricule;
    private String status;
}
