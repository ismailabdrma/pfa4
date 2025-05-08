package com.amn.dto;

import com.amn.entity.MedicalRecord;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {
    private Long id;
    private String reason;
    private String diagnosis;
    private String notes;
    private LocalDate creationDate;
    private String doctorName;

    public static MedicalRecordDTO fromEntity(MedicalRecord record) {
        return MedicalRecordDTO.builder()
                .id(record.getId())
                .reason(record.getReason())
                .diagnosis(record.getDiagnosis())
                .notes(record.getNotes())
                .creationDate(record.getCreationDate())
                .doctorName(record.getDoctor() != null ? record.getDoctor().getFullName() : "Docteur inconnu")
                .build();
    }
}
