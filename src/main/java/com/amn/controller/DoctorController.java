package com.amn.controller;

import com.amn.dto.*;
import com.amn.entity.*;
import com.amn.repository.MedicalRecordRepository;
import com.amn.repository.PrescriptionRepository;
import com.amn.service.DoctorService;
import com.amn.service.FileStorageService;
import com.amn.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final PrescriptionService prescriptionService;
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * ✅ Create or update a medical record
     */
    @PostMapping("/add-record/{patientId}")
    public ResponseEntity<MedicalRecord> createMedicalRecord(
            @PathVariable Long patientId,
            @RequestBody MedicalRecord record,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String doctorEmail = userDetails.getUsername();
        return ResponseEntity.ok(doctorService.createMedicalRecord(patientId, record, doctorEmail));
    }

    /**
     * ✅ Write a new prescription
     */
    @PostMapping("/prescribe/{patientId}/{recordId}")
    public ResponseEntity<PrescriptionDTO> writePrescription(
            @PathVariable Long patientId,
            @PathVariable Long recordId,
            @RequestBody PrescriptionDTO prescriptionDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        PrescriptionDTO createdPrescription = doctorService.writePrescription(patientId, recordId, prescriptionDTO, email);
        return ResponseEntity.ok(createdPrescription);
    }

    /**
     * ✅ Create a prescription with medications
     */
    @PostMapping("/consultation/{patientId}/{recordId}/prescription")
    public ResponseEntity<PrescriptionDTO> createPrescriptionWithMedications(
            @PathVariable Long patientId,
            @PathVariable Long recordId,
            @RequestBody PrescriptionRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        PrescriptionDTO dto = prescriptionService.createPrescriptionWithMedications(patientId, recordId, request, email);
        return ResponseEntity.ok(dto);
    }

    /**
     * ✅ Get Patient Full Profile
     */
    @GetMapping("/full-profile")
    public ResponseEntity<PatientProfileDTO> getFullPatientProfile(
            @RequestParam String cin,
            @RequestParam String fullName
    ) {
        return ResponseEntity.ok(doctorService.getFullPatientProfile(cin, fullName));
    }

    /**
     * ✅ Get a single prescription by ID
     */
    @GetMapping("/prescription/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDTO prescription = prescriptionService.getById(id);
        return ResponseEntity.ok(prescription);
    }

    /**
     * ✅ Get consultation by record ID including associated prescriptions
     */
    @GetMapping("/consultation/{id}")
    public ResponseEntity<ConsultationWithPrescriptionDTO> getConsultationById(@PathVariable Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        List<PrescriptionDTO> prescriptions = prescriptionService.getPrescriptionsByRecordId(id);

        String doctorName = record.getDoctor() != null ? record.getDoctor().getFullName() : "Docteur inconnu";

        ConsultationWithPrescriptionDTO dto = new ConsultationWithPrescriptionDTO(
                record.getId(),
                record.getReason(),
                record.getDiagnosis(),
                record.getNotes(),
                record.getCreationDate().atStartOfDay(),
                doctorName,
                prescriptions
        );

        return ResponseEntity.ok(dto);
    }

    /**
     * ✅ Get Patient ID by CIN and Full Name
     */
    @GetMapping("/get-id")
    public ResponseEntity<Long> getPatientId(@RequestParam String cin, @RequestParam String fullName) {
        return ResponseEntity.ok(doctorService.getPatientIdByCinAndName(cin, fullName));
    }

    /**
     * ✅ Upload Scan File
     */
    @PostMapping("/upload-scan-file")
    public ResponseEntity<Scan> uploadScanFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId
    ) {
        return ResponseEntity.ok(doctorService.uploadScanLocally(file, title, description, folderId));
    }

    /**
     * ✅ Upload Analysis File
     */
    @PostMapping("/upload-analysis")
    public ResponseEntity<AnalysisDTO> uploadAnalysis(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId
    ) {
        return ResponseEntity.ok(doctorService.uploadAnalysisLocally(file, title, description, folderId));
    }

    /**
     * ✅ Upload Surgery File
     */
    @PostMapping("/upload-surgery")
    public ResponseEntity<Surgery> uploadSurgery(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("folderId") Long folderId
    ) {
        return ResponseEntity.ok(doctorService.uploadSurgeryLocally(file, title, description, folderId));
    }
    @GetMapping("/me")
    public ResponseEntity<DoctorDTO> getCurrentDoctorProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Doctor doctor = doctorService.findByEmail(email);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(new DoctorDTO(doctor));
    }



}
