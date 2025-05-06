package com.amn.dto;

import com.amn.entity.Vaccination;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaccinationDTO {
    private Long id;
    private String vaccineName;
    private int doseNumber;
    private String manufacturer;
    private String vaccinationDate;

    public static VaccinationDTO fromEntity(Vaccination v) {
        return VaccinationDTO.builder()
                .id(v.getId())
                .vaccineName(v.getVaccineName())
                .doseNumber(v.getDoseNumber())
                .manufacturer(v.getManufacturer())
                .vaccinationDate(v.getVaccinationDate().toString())
                .build();
    }
}
