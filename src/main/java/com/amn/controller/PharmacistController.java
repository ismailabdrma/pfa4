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

    // ✅ View all prescriptions
    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        return ResponseEntity.ok(pharmacistService.viewPrescriptions());
    }

    // ✅ View prescriptions by patient CIN and full name
    @GetMapping("/prescriptions/by-patient")
    public ResponseEntity<List<Prescription>> getPrescriptionsByCinAndName(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(
                pharmacistService.viewPrescriptionsByPatient(cin, fullName)
        );
    }

    // ✅ Dispense a prescription
    @PostMapping("/dispense")
    public ResponseEntity<Prescription> dispensePrescription(
            @RequestParam Long prescriptionId,
            @RequestParam Long pharmacistId
    ) {
        return ResponseEntity.ok(
                pharmacistService.dispensePrescription(prescriptionId, pharmacistId)
        );
    }

    // ✅ Add Over-The-Counter (OTC) medication
    @PostMapping("/add-otc")
    public ResponseEntity<Medication> addOTCMedication(
            @RequestBody Medication medication,
            @RequestParam Long pharmacistId
    ) {
        return ResponseEntity.ok(
                pharmacistService.addOTCMedication(medication, pharmacistId)
        );
    }
}
