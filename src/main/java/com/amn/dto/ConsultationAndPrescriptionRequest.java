package com.amn.dto;

import com.amn.entity.MedicalRecord;
import lombok.Data;

@Data
public class ConsultationAndPrescriptionRequest {
    private MedicalRecord consultation;
    private PrescriptionRequest prescription;
}