// ✅ PharmacistController.java
package com.amn.controller;

import com.amn.entity.Medication;
import com.amn.entity.Prescription;
import com.amn.service.PharmacistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacist")
@RequiredArgsConstructor
public class PharmacistController {

    private final PharmacistService pharmacistService;

    // ✅ View prescriptions by patient CIN and name
    @GetMapping("/prescriptions/by-patient")
    public ResponseEntity<List<Prescription>> getPrescriptionsByCinAndName(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(pharmacistService.viewPrescriptionsByPatient(cin, fullName));
    }

    // ✅ Dispense prescription
    @PostMapping("/dispense")
    public ResponseEntity<Prescription> dispensePrescription(
            @RequestParam Long prescriptionId,
            @RequestParam Long pharmacistId
    ) {
        return ResponseEntity.ok(pharmacistService.dispensePrescription(prescriptionId, pharmacistId));
    }

    // ✅ Add OTC medication + log it as a prescription
    @PostMapping("/add-otc")
    public ResponseEntity<Medication> addOTC(
            @RequestBody Medication medication,
            @RequestParam Long pharmacistId,
            @RequestParam Long patientId
    ) {
        return ResponseEntity.ok(pharmacistService.addOTCMedication(medication, pharmacistId, patientId));
    }
}
