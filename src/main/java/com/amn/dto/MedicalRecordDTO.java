package com.amn.dto;

import com.amn.entity.MedicalRecord;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<PrescriptionDTO> prescriptions;

    public static MedicalRecordDTO fromEntity(MedicalRecord record) {
        return MedicalRecordDTO.builder()
                .id(record.getId())
                .reason(record.getReason())
                .diagnosis(record.getDiagnosis())
                .notes(record.getNotes())
                .creationDate(record.getCreationDate())
                .doctorName(record.getDoctor() != null ? record.getDoctor().getFullName() : "Docteur inconnu")
                .prescriptions(
                        record.getPrescriptions() != null
                                ? record.getPrescriptions()
                                .stream()
                                .map(PrescriptionDTO::fromEntity)
                                .collect(Collectors.toList())
                                : null
                )
                .build();
    }
}
