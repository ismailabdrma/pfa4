package com.amn.controller;

import com.amn.dto.PrescriptionDTO;
import com.amn.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    /**
     * ✅ Get all prescriptions by CIN and Full Name
     */
    @GetMapping("/patient")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByPatient(
            @RequestParam String cin,
            @RequestParam String fullName) {
        List<PrescriptionDTO> prescriptions = prescriptionService.getPrescriptionsByPatient(cin, fullName);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * ✅ Get a single prescription by ID, including patient info
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDTO prescription = prescriptionService.getById(id);
        return ResponseEntity.ok(prescription);
    }

    /**
     * ✅ Fetch all prescriptions without any filter
     */
    @GetMapping("/all")
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        List<PrescriptionDTO> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }
}
