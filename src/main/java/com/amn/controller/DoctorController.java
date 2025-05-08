package com.amn.controller;

import com.amn.dto.*;
import com.amn.entity.*;
import com.amn.repository.MedicalRecordRepository;
import com.amn.repository.PrescriptionRepository;
import com.amn.service.DoctorService;
import com.amn.service.FileStorageService;
import com.amn.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionService prescriptionService;

    @GetMapping("/folder")
    public ResponseEntity<MedicalFolder> getPatientFolder(@RequestParam String cin, @RequestParam String fullName) {
        return ResponseEntity.ok(doctorService.getMedicalFolderByCinAndName(cin, fullName));
    }

    @PostMapping("/add-record/{patientId}")
    public ResponseEntity<MedicalRecord> createMedicalRecord(
            @PathVariable Long patientId,
            @RequestBody MedicalRecord record,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String doctorEmail = userDetails.getUsername();
        return ResponseEntity.ok(doctorService.createMedicalRecord(patientId, record, doctorEmail));
    }



    @PostMapping("/prescribe/{patientId}")
    public ResponseEntity<Prescription> writePrescription(
            @PathVariable Long patientId,
            @RequestBody Prescription prescription,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(doctorService.writePrescription(patientId, prescription, email));
    }

    @PostMapping("/consultation/{patientId}/{recordId}/prescription")
    public ResponseEntity<PrescriptionDTO> createPrescriptionWithMedications(
            @PathVariable Long patientId,
            @PathVariable Long recordId,
            @RequestBody PrescriptionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        PrescriptionDTO dto = prescriptionService.createPrescriptionWithMedications(patientId, recordId, request, email);
        return ResponseEntity.ok(dto);
    }



    @GetMapping("/get-id")
    public ResponseEntity<Long> getPatientId(@RequestParam String cin, @RequestParam String fullName) {
        return ResponseEntity.ok(doctorService.getPatientIdByCinAndName(cin, fullName));
    }

    @PostMapping("/create-folder/{cin}")
    public ResponseEntity<?> createFolderWithDetails(@PathVariable String cin, @RequestParam String fullName, @RequestBody Map<String, String> body) {
        Long patientId = doctorService.getPatientIdByCinAndName(cin, fullName);
        doctorService.createOrUpdateFolderWithPatientDetails(patientId, body);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/full-profile")
    public ResponseEntity<PatientProfileDTO> getFullPatientProfile(@RequestParam String cin, @RequestParam String fullName) {
        return ResponseEntity.ok(doctorService.getFullPatientProfile(cin, fullName));
    }

    @PostMapping("/upload-scan")
    public ResponseEntity<Scan> uploadScanLink(@RequestBody Scan scan, @RequestParam Long folderId) {
        return ResponseEntity.ok(fileStorageService.saveScanFromLink(scan, folderId));
    }

    @PostMapping("/upload-scan-file")
    public ResponseEntity<Scan> uploadScanFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId) {
        return ResponseEntity.ok(doctorService.uploadScanLocally(file, title, description, folderId));
    }

    @PostMapping("/upload-analysis")
    public ResponseEntity<AnalysisDTO> uploadAnalysis(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId) {
        return ResponseEntity.ok(doctorService.uploadAnalysisLocally(file, title, description, folderId));
    }

    @PostMapping("/upload-surgery")
    public ResponseEntity<Surgery> uploadSurgery(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId) {
        return ResponseEntity.ok(doctorService.uploadSurgeryLocally(file, title, description, folderId));
    }

    @PostMapping("/add-vaccination")
    public ResponseEntity<Vaccination> addVaccination(@RequestParam String vaccineName, @RequestParam int doseNumber, @RequestParam String manufacturer, @RequestParam String date, @RequestParam Long folderId) {
        LocalDate vaccinationDate = LocalDate.parse(date);
        return ResponseEntity.ok(doctorService.addVaccinationRecord(folderId, vaccineName, doseNumber, manufacturer, vaccinationDate));
    }
    @GetMapping("/scan/{id}")
    public ResponseEntity<ScanDTO> getScanById(@PathVariable Long id) {
        Scan scan = fileStorageService.getScanById(id);
        return ResponseEntity.ok(ScanDTO.fromEntity(scan));
    }


    @GetMapping("/analysis/{id}")
    public ResponseEntity<AnalysisDTO> getAnalysisById(@PathVariable Long id) {
        return ResponseEntity.ok(AnalysisDTO.fromEntity(fileStorageService.getAnalysisById(id)));
    }

    @GetMapping("/surgery/{id}")
    public ResponseEntity<SurgeryDTO> getSurgeryById(@PathVariable Long id) {
        return ResponseEntity.ok(SurgeryDTO.fromEntity(fileStorageService.getSurgeryById(id)));
    }

    @GetMapping("/scans")
    public ResponseEntity<List<ScanDTO>> getScansByFolder(@RequestParam Long folderId) {
        return ResponseEntity.ok(fileStorageService.getScansByFolderId(folderId).stream().map(ScanDTO::fromEntity).toList());
    }

    @GetMapping("/analyses")
    public ResponseEntity<List<AnalysisDTO>> getAnalysesByFolder(@RequestParam Long folderId) {
        return ResponseEntity.ok(fileStorageService.getAnalysesByFolderId(folderId).stream().map(AnalysisDTO::fromEntity).toList());
    }

    @GetMapping("/surgeries")
    public ResponseEntity<List<SurgeryDTO>> getSurgeriesByFolder(@RequestParam Long folderId) {
        return ResponseEntity.ok(fileStorageService.getSurgeriesByFolderId(folderId).stream().map(SurgeryDTO::fromEntity).toList());
    }

    @GetMapping("/me")
    public ResponseEntity<Doctor> getDoctorProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Doctor doctor = doctorService.getCurrentDoctorProfile(email);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/prescription/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Prescription not found"));
        return ResponseEntity.ok(PrescriptionDTO.fromEntity(prescription));
    }

    @GetMapping("/consultation/{id}")
    public ResponseEntity<ConsultationWithPrescriptionDTO> getConsultationById(@PathVariable Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        Prescription prescription = prescriptionRepository.findByMedicalRecordId(id).orElse(null);

        String doctorName = (record.getDoctor() != null)
                ? record.getDoctor().getFullName()
                : "Docteur inconnu";

        ConsultationWithPrescriptionDTO dto = new ConsultationWithPrescriptionDTO(
                record.getId(),
                record.getReason(),
                record.getDiagnosis(),
                record.getNotes(),
                record.getCreationDate().atStartOfDay(),
                doctorName,
                prescription != null ? PrescriptionDTO.fromEntity(prescription) : null
        );

        return ResponseEntity.ok(dto);
    }



}
