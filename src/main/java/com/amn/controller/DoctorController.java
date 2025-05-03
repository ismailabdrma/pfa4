package com.amn.controller;

import com.amn.dto.PatientProfileDTO;
import com.amn.entity.MedicalFolder;
import com.amn.entity.MedicalRecord;
import com.amn.entity.Prescription;
import com.amn.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/folder")
    public ResponseEntity<MedicalFolder> getPatientFolder(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(
                doctorService.getMedicalFolderByCinAndName(cin, fullName)
        );
    }

    @PostMapping("/add-record/{patientId}")
    public ResponseEntity<MedicalRecord> createMedicalRecord(
            @PathVariable Long patientId,
            @RequestBody MedicalRecord record
    ) {
        return ResponseEntity.ok(
                doctorService.createMedicalRecord(patientId, record)
        );
    }

    @PostMapping("/prescribe/{patientId}")
    public ResponseEntity<Prescription> writePrescription(
            @PathVariable Long patientId,
            @RequestBody Prescription prescription
    ) {
        return ResponseEntity.ok(
                doctorService.writePrescription(patientId, prescription)
        );
    }

    @GetMapping("/get-id")
    public ResponseEntity<Long> getPatientId(@RequestParam String cin, @RequestParam String fullName) {
        return ResponseEntity.ok(
                doctorService.getPatientIdByCinAndName(cin, fullName)
        );
    }

    // ✅ Create folder with full patient details
    @PostMapping("/create-folder/{cin}")
    public ResponseEntity<?> createFolderWithDetails(
            @PathVariable String cin,
            @RequestParam String fullName,
            @RequestBody Map<String, String> body
    ) {
        Long patientId = doctorService.getPatientIdByCinAndName(cin, fullName);

        // Doctor fills missing data here (patient update logic happens inside service)
        doctorService.createOrUpdateFolderWithPatientDetails(patientId, body);

        return ResponseEntity.ok().build();
    }

    // ✅ Full Profile DTO (for Angular profile view)
    @GetMapping("/full-profile")
    public ResponseEntity<PatientProfileDTO> getFullPatientProfile(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(
                doctorService.getFullPatientProfile(cin, fullName)
        );
    }
}
