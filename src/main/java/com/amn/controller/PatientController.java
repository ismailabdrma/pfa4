package com.amn.controller;

import com.amn.dto.PatientProfileDTO;
import com.amn.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * Get complete patient profile by CIN
     */
    @GetMapping("/profile")
    public ResponseEntity<PatientProfileDTO> getPatientProfile(@RequestParam String cin) {
        PatientProfileDTO profile = patientService.getPatientProfileByCin(cin);
        return ResponseEntity.ok(profile);
    }
}
