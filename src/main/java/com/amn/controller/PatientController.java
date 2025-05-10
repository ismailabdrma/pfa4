package com.amn.controller;

import com.amn.dto.*;
import com.amn.entity.Patient;
import com.amn.entity.Vaccination;
import com.amn.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * ✅ Get complete patient profile by CIN
     */
    @GetMapping("/profile-by-cin")
    public ResponseEntity<PatientProfileDTO> getPatientProfileByCin(@RequestParam String cin) {
        System.out.println("Fetching patient profile by CIN: " + cin);
        PatientProfileDTO profile = patientService.getPatientProfileByCin(cin);
        return ResponseEntity.ok(profile);
    }

    /**
     * ✅ Get full patient profile by Email
     */
    @GetMapping("/profile-by-email")
    public ResponseEntity<PatientProfileDTO> getPatientProfileByEmail(@RequestParam String email) {
        System.out.println("Fetching patient profile by Email: " + email);
        PatientProfileDTO profile = patientService.getPatientProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    /**
     * ✅ Get current patient data by Email
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, String>> getCurrentPatient(@RequestParam String email) {
        System.out.println("Fetching current patient data by email: " + email);
        Patient patient = patientService.getCurrentPatient(email);

        Map<String, String> response = new HashMap<>();
        response.put("cin", patient.getCin());
        response.put("email", patient.getEmail());

        return ResponseEntity.ok(response);
    }

    /**
     * ✅ Get patient profile by Folder ID
     */
    @GetMapping("/profile-by-folder/{folderId}")
    public ResponseEntity<PatientProfileDTO> getPatientProfileByFolderId(@PathVariable Long folderId) {
        System.out.println("Fetching patient profile by folder ID: " + folderId);
        PatientProfileDTO profile = patientService.getPatientProfileByFolderId(folderId);
        return ResponseEntity.ok(profile);
    }

    /**
     * ✅ Get all medical records for a patient by Folder ID
     */
    @GetMapping("/records/{folderId}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecords(@PathVariable Long folderId) {
        System.out.println("Fetching medical records for folder ID: " + folderId);
        List<MedicalRecordDTO> records = patientService.getMedicalRecords(folderId);
        return ResponseEntity.ok(records);
    }

    /**
     * ✅ Get all prescriptions for a patient by Folder ID
     */
    @GetMapping("/prescriptions/{folderId}")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptions(@PathVariable Long folderId) {
        System.out.println("Fetching prescriptions for folder ID: " + folderId);
        List<PrescriptionDTO> prescriptions = patientService.getPrescriptions(folderId);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * ✅ Get all scans for a patient by Folder ID
     */
    @GetMapping("/scans/{folderId}")
    public ResponseEntity<List<ScanDTO>> getScans(@PathVariable Long folderId) {
        System.out.println("Fetching scans for folder ID: " + folderId);
        List<ScanDTO> scans = patientService.getScans(folderId);
        return ResponseEntity.ok(scans);
    }

    /**
     * ✅ Get all analyses for a patient by Folder ID
     */
    @GetMapping("/analyses/{folderId}")
    public ResponseEntity<List<AnalysisDTO>> getAnalyses(@PathVariable Long folderId) {
        System.out.println("Fetching analyses for folder ID: " + folderId);
        List<AnalysisDTO> analyses = patientService.getAnalyses(folderId);
        return ResponseEntity.ok(analyses);
    }

    /**
     * ✅ Get all surgeries for a patient by Folder ID
     */
    @GetMapping("/surgeries/{folderId}")
    public ResponseEntity<List<SurgeryDTO>> getSurgeries(@PathVariable Long folderId) {
        System.out.println("Fetching surgeries for folder ID: " + folderId);
        List<SurgeryDTO> surgeries = patientService.getSurgeries(folderId);
        return ResponseEntity.ok(surgeries);
    }

    /**
     * ✅ Get all vaccinations for a patient by Folder ID
     */
    @GetMapping("/vaccinations/{folderId}")
    public ResponseEntity<List<Vaccination>> getVaccinations(@PathVariable Long folderId) {
        System.out.println("Fetching vaccinations for folder ID: " + folderId);
        List<Vaccination> vaccinations = patientService.getVaccinations(folderId);
        return ResponseEntity.ok(vaccinations);
    }
    @GetMapping("/basic-info")
    public ResponseEntity<PatientBasicInfoDTO> getPatientBasicInfo(@RequestParam String cin) {
        return ResponseEntity.ok(patientService.getPatientBasicInfo(cin));
    }
    /**
     * ✅ Update Patient Basic Info (Address, Email, Phone)
     */
    @PutMapping("/update-basic-info")
    public ResponseEntity<String> updatePatientBasicInfo(@RequestParam String cin, @RequestBody PatientBasicInfoDTO basicInfo) {
        System.out.println("Updating patient basic info for CIN: " + cin);
        patientService.updatePatientBasicInfo(cin, basicInfo);
        return ResponseEntity.ok("Patient information updated successfully.");
    }


}
