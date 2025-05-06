package com.amn.controller;

import com.amn.dto.PatientProfileDTO;
import com.amn.dto.ScanDTO;
import com.amn.dto.AnalysisDTO;
import com.amn.dto.SurgeryDTO;
import com.amn.entity.*;
import com.amn.service.DoctorService;
import com.amn.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final FileStorageService fileStorageService;

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
    public ResponseEntity<Long> getPatientId(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(
                doctorService.getPatientIdByCinAndName(cin, fullName)
        );
    }

    @PostMapping("/create-folder/{cin}")
    public ResponseEntity<?> createFolderWithDetails(
            @PathVariable String cin,
            @RequestParam String fullName,
            @RequestBody Map<String, String> body
    ) {
        Long patientId = doctorService.getPatientIdByCinAndName(cin, fullName);
        doctorService.createOrUpdateFolderWithPatientDetails(patientId, body);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/full-profile")
    public ResponseEntity<PatientProfileDTO> getFullPatientProfile(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(
                doctorService.getFullPatientProfile(cin, fullName)
        );
    }

    @PostMapping("/upload-scan")
    public ResponseEntity<Scan> uploadScanLink(
            @RequestBody Scan scan,
            @RequestParam Long folderId
    ) {
        return ResponseEntity.ok(fileStorageService.saveScanFromLink(scan, folderId));
    }

    @PostMapping("/upload-scan-file")
    public ResponseEntity<Scan> uploadScanFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId
    ) {
        return ResponseEntity.ok(
                doctorService.uploadScanLocally(file, title, description, folderId)
        );
    }

    @PostMapping("/upload-analysis")
    public ResponseEntity<Analysis> uploadAnalysis(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId
    ) {
        return ResponseEntity.ok(
                doctorService.uploadAnalysisLocally(file, title, description, folderId)
        );
    }

    @PostMapping("/upload-surgery")
    public ResponseEntity<Surgery> uploadSurgery(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId
    ) {
        return ResponseEntity.ok(
                doctorService.uploadSurgeryLocally(file, title, description, folderId)
        );
    }

    @PostMapping("/add-vaccination")
    public ResponseEntity<Vaccination> addVaccination(
            @RequestParam String vaccineName,
            @RequestParam int doseNumber,
            @RequestParam String manufacturer,
            @RequestParam String date,
            @RequestParam Long folderId
    ) {
        LocalDate vaccinationDate = LocalDate.parse(date);
        return ResponseEntity.ok(
                doctorService.addVaccinationRecord(folderId, vaccineName, doseNumber, manufacturer, vaccinationDate)
        );
    }

    @GetMapping("/scan/{id}")
    public ResponseEntity<Scan> getScanById(@PathVariable Long id) {
        return ResponseEntity.ok(fileStorageService.getScanById(id));
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<Analysis> getAnalysisById(@PathVariable Long id) {
        return ResponseEntity.ok(fileStorageService.getAnalysisById(id));
    }

    @GetMapping("/surgery/{id}")
    public ResponseEntity<Surgery> getSurgeryById(@PathVariable Long id) {
        return ResponseEntity.ok(fileStorageService.getSurgeryById(id));
    }

    @GetMapping("/scans")
    public ResponseEntity<List<ScanDTO>> getScansByFolder(@RequestParam Long folderId) {
        return ResponseEntity.ok(
                fileStorageService.getScansByFolderId(folderId).stream()
                        .map(ScanDTO::fromEntity)
                        .toList()
        );
    }

    @GetMapping("/analyses")
    public ResponseEntity<List<AnalysisDTO>> getAnalysesByFolder(@RequestParam Long folderId) {
        return ResponseEntity.ok(
                fileStorageService.getAnalysesByFolderId(folderId).stream()
                        .map(AnalysisDTO::fromEntity)
                        .toList()
        );
    }

    @GetMapping("/surgeries")
    public ResponseEntity<List<SurgeryDTO>> getSurgeriesByFolder(@RequestParam Long folderId) {
        return ResponseEntity.ok(
                fileStorageService.getSurgeriesByFolderId(folderId).stream()
                        .map(SurgeryDTO::fromEntity)
                        .toList()
        );
    }
}
