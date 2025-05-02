package com.amn.controller;

import com.amn.entity.MedicalFolder;
import com.amn.entity.MedicalRecord;
import com.amn.entity.Prescription;
import com.amn.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // ✅ View patient's medical folder using CIN and name
    @GetMapping("/folder")
    public ResponseEntity<MedicalFolder> getPatientFolder(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(
                doctorService.getMedicalFolderByCinAndName(cin, fullName)
        );
    }

    // ✅ Create a medical record
    @PostMapping("/add-record/{patientId}")
    public ResponseEntity<MedicalRecord> createMedicalRecord(
            @PathVariable Long patientId,
            @RequestBody MedicalRecord record
    ) {
        return ResponseEntity.ok(
                doctorService.createMedicalRecord(patientId, record)
        );
    }

    // ✅ Write a prescription
    @PostMapping("/prescribe/{patientId}")
    public ResponseEntity<Prescription> writePrescription(
            @PathVariable Long patientId,
            @RequestBody Prescription prescription
    ) {
        return ResponseEntity.ok(
                doctorService.writePrescription(patientId, prescription)
        );
    }
}
