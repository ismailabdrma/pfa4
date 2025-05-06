package com.amn.dto;

import com.amn.entity.MedicalRecord;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MedicalRecordDTO {
    private Long id;
    private String reason;
    private String diagnosis;
    private String notes;
    private LocalDate creationDate;

    public static MedicalRecordDTO fromEntity(MedicalRecord entity) {
        return MedicalRecordDTO.builder()
                .id(entity.getId())
                .reason(entity.getReason())
                .diagnosis(entity.getDiagnosis())
                .notes(entity.getNotes())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
