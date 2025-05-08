package com.amn.dto;

import com.amn.entity.Medication;
import com.amn.entity.Prescription;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {
    private Prescription prescription;
    private List<Medication> medications;
}
