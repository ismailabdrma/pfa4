package com.amn.dto;

import com.amn.entity.Prescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationWithPrescriptionDTO {
    private Long id;
    private String reason;
    private String diagnosis;
    private String notes;
    private LocalDateTime creationDate;
    private String doctorName; // ✅ Add this
    private List<PrescriptionDTO> prescriptions;

}
