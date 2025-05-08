package com.amn.controller;

import com.amn.dto.PrescriptionDTO;
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

    /**
     * ✅ Get prescriptions for a patient by CIN and Full Name.
     * Optional query parameter `status` can be `ALL` or `DISPENSED`.
     */
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByPatient(
            @RequestParam String cin,
            @RequestParam String fullName,
            @RequestParam(defaultValue = "ALL") String status) {

        List<PrescriptionDTO> prescriptions;

        if ("DISPENSED".equalsIgnoreCase(status)) {
            prescriptions = pharmacistService.getDispensedPrescriptionsByCinAndName(cin, fullName);
        } else {
            prescriptions = pharmacistService.getPrescriptionsByCinAndName(cin, fullName);
        }

        return ResponseEntity.ok(prescriptions);
    }

    /**
     * ✅ Mark a prescription as DISPENSED
     */
    @PutMapping("/prescriptions/{id}/dispense")
    public ResponseEntity<PrescriptionDTO> markAsDispensed(@PathVariable Long id) {
        PrescriptionDTO updatedPrescription = pharmacistService.markAsDispensed(id);
        return ResponseEntity.ok(updatedPrescription);
    }

    /**
     * ✅ Get a single prescription by ID
     */
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDTO prescription = pharmacistService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }
}
