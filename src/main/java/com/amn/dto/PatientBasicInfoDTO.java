package com.amn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientBasicInfoDTO {
    private Long id;
    private String fullName;
    private String email;
    private String cin;
    private String address;
    private String phone;
    private String bloodType;
}
