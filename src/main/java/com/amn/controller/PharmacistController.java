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
     * ✅ Get prescriptions by CIN and Full Name with status filter
     */
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptions(
            @RequestParam String cin,
            @RequestParam String fullName,
            @RequestParam(defaultValue = "ALL") String status) {

        System.out.println("Fetching prescriptions with status: " + status);

        List<PrescriptionDTO> prescriptions;
        if ("DISPENSED".equals(status)) {
            prescriptions = pharmacistService.getDispensedPrescriptionsByCinAndName(cin, fullName);
        } else if ("PENDING".equals(status)) {
            prescriptions = pharmacistService.getPendingPrescriptionsByCinAndName(cin, fullName);
        } else {
            prescriptions = pharmacistService.getPrescriptionsByCinAndName(cin, fullName);
        }

        System.out.println("Found " + prescriptions.size() + " prescriptions");
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * ✅ Mark a prescription as dispensed
     */
    @PutMapping("/prescriptions/{id}/dispense")
    public ResponseEntity<PrescriptionDTO> markAsDispensed(@PathVariable Long id) {
        PrescriptionDTO updatedPrescription = pharmacistService.markAsDispensed(id);
        return ResponseEntity.ok(updatedPrescription);
    }

    /**
     * ✅ Get a specific prescription by ID
     */
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDTO prescription = pharmacistService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }
}