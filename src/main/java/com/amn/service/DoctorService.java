// DoctorService.java
package com.amn.service;

import com.amn.dto.*;
import com.amn.entity.*;
import com.amn.entity.enums.FileType;
import com.amn.entity.enums.OTPStatus;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicalFolderRepository medicalFolderRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final OTPRepository otpRepository;
    private final ScanRepository scanRepository;
    private final AnalysisRepository analysisRepository;
    private final SurgeryRepository surgeryRepository;
    private final VaccinationRepository vaccinationRepository;

    public Optional<Doctor> login(String email, String password) {
        return doctorRepository.findByEmail(email)
                .filter(d -> password.equals(d.getPassword()));
    }

    public boolean requestAccessToPatient(String cin) {
        return !patientRepository.findAllByCin(cin).isEmpty();
    }

    public Optional<MedicalFolder> viewMedicalDocumentAfterOtp(String cin, String code) {
        Patient patient = patientRepository.findAllByCin(cin).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        OTP otp = otpRepository.findTopByPatientIdOrderByExpirationDesc(patient.getId())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otp.getCode().equals(code)
                && otp.getExpiration().isAfter(LocalDateTime.now())
                && otp.getStatus() == OTPStatus.PENDING) {

            otp.setStatus(OTPStatus.VERIFIED);
            otpRepository.save(otp);

            return medicalFolderRepository.findByPatientId(patient.getId());
        }

        throw new RuntimeException("Invalid or expired OTP");
    }

    public MedicalRecord createMedicalRecord(Long patientId, MedicalRecord record) {
        MedicalFolder folder = medicalFolderRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Medical folder not found"));

        record.setCreationDate(LocalDate.now());
        record.setMedicalFolder(folder);
        return medicalRecordRepository.save(record);
    }

    public Prescription writePrescription(Long patientId, Prescription prescription) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        prescription.setPrescribedDate(LocalDateTime.now());
        prescription.setPatient(patient);
        return prescriptionRepository.save(prescription);
    }

    public MedicalFolder getMedicalFolderByCinAndName(String cin, String fullName) {
        Patient patient = patientRepository.findAllByCin(cin).stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(fullName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found or name mismatch"));

        return medicalFolderRepository.findByPatientId(patient.getId())
                .orElseGet(() -> {
                    MedicalFolder newFolder = new MedicalFolder();
                    newFolder.setPatient(patient);
                    newFolder.setCreationDate(LocalDate.now());
                    return medicalFolderRepository.save(newFolder);
                });
    }

    public Long getPatientIdByCinAndName(String cin, String fullName) {
        return patientRepository.findAllByCin(cin).stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(fullName))
                .findFirst()
                .map(Patient::getId)
                .orElseThrow(() -> new RuntimeException("Patient not found or name mismatch"));
    }

    public void createOrUpdateFolderWithPatientDetails(Long patientId, Map<String, String> body) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setBloodType(body.get("bloodType"));
        patient.setAllergies(body.get("allergies"));
        patient.setChronicDiseases(body.get("chronicDiseases"));
        patient.setEmergencyContact(body.get("emergencyContact"));
        patient.setHasHeartProblem(Boolean.parseBoolean(body.get("hasHeartProblem")));
        patient.setHasSurgery(Boolean.parseBoolean(body.get("hasSurgery")));

        if (body.get("birthDate") != null) {
            patient.setBirthDate(LocalDate.parse(body.get("birthDate")));
        }

        patientRepository.save(patient);

        if (medicalFolderRepository.findByPatientId(patientId).isEmpty()) {
            MedicalFolder folder = new MedicalFolder();
            folder.setPatient(patient);
            folder.setCreationDate(LocalDate.now());
            medicalFolderRepository.save(folder);
        }
    }

    public PatientProfileDTO getFullPatientProfile(String cin, String fullName) {
        Patient patient = patientRepository.findAllByCin(cin).stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(fullName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found or name mismatch"));

        MedicalFolder folder = medicalFolderRepository.findByPatientId(patient.getId())
                .orElseGet(() -> {
                    MedicalFolder newFolder = new MedicalFolder();
                    newFolder.setPatient(patient);
                    newFolder.setCreationDate(LocalDate.now());
                    return medicalFolderRepository.save(newFolder);
                });

        List<ScanDTO> scanDTOs = scanRepository.findAllByMedicalFolderId(folder.getId()).stream()
                .map(ScanDTO::fromEntity)
                .toList();

        List<AnalysisDTO> analysisDTOs = analysisRepository.findAllByMedicalFolderId(folder.getId()).stream()
                .map(AnalysisDTO::fromEntity)
                .toList();

        List<SurgeryDTO> surgeryDTOs = surgeryRepository.findAllByMedicalFolderId(folder.getId()).stream()
                .map(SurgeryDTO::fromEntity)
                .toList();

        return PatientProfileDTO.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .cin(patient.getCin())
                .bloodType(patient.getBloodType())
                .birthDate(patient.getBirthDate() != null ? patient.getBirthDate().toString() : null)
                .emergencyContact(patient.getEmergencyContact())
                .allergies(patient.getAllergies())
                .chronicDiseases(patient.getChronicDiseases())
                .hasHeartProblem(patient.isHasHeartProblem())
                .hasSurgery(patient.isHasSurgery())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .medicalFolderId(folder.getId())
                .medicalRecords(folder.getMedicalRecords())
                .vaccinations(vaccinationRepository.findAllByMedicalFolderId(folder.getId()))
                .visitLogs(folder.getVisitLogs())
                .scans(scanDTOs)
                .analyses(analysisDTOs)
                .surgeries(surgeryDTOs)
                .prescriptions(prescriptionRepository.findAllByPatientId(patient.getId()))
                .build();
    }

    public Scan uploadScanLocally(MultipartFile file, String title, String description, Long folderId) {
        try {
            String uploadDir = "C:/amn/uploads/scans/";
            new File(uploadDir).mkdirs();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            MedicalFolder folder = medicalFolderRepository.findById(folderId)
                    .orElseThrow(() -> new RuntimeException("Folder not found"));

            Scan scan = Scan.builder()
                    .title(title)
                    .description(description)
                    .uploadDate(LocalDate.now())
                    .fileType(FileType.SCAN)
                    .url("/uploads/scans/" + fileName)
                    .medicalFolder(folder)
                    .build();

            return scanRepository.save(scan);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    public Analysis uploadAnalysisLocally(MultipartFile file, String title, String description, Long folderId) {
        try {
            String uploadDir = "C:/amn/uploads/analyses/";
            new File(uploadDir).mkdirs();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            MedicalFolder folder = medicalFolderRepository.findById(folderId)
                    .orElseThrow(() -> new RuntimeException("Folder not found"));

            Analysis analysis = Analysis.builder()
                    .title(title)
                    .description(description)
                    .uploadDate(LocalDate.now())
                    .fileType(FileType.ANALYSIS)
                    .url("/uploads/analyses/" + fileName)
                    .medicalFolder(folder)
                    .build();

            return analysisRepository.save(analysis);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    public Surgery uploadSurgeryLocally(MultipartFile file, String title, String description, Long folderId) {
        try {
            String uploadDir = "C:/amn/uploads/surgeries/";
            new File(uploadDir).mkdirs();

            String fileName = file != null ? System.currentTimeMillis() + "_" + file.getOriginalFilename() : null;
            if (file != null) file.transferTo(new File(uploadDir + fileName));

            MedicalFolder folder = medicalFolderRepository.findById(folderId)
                    .orElseThrow(() -> new RuntimeException("Folder not found"));

            Surgery surgery = Surgery.builder()
                    .title(title)
                    .description(description)
                    .uploadDate(LocalDate.now())
                    .fileType(FileType.SURGERY)
                    .url(fileName != null ? "/uploads/surgeries/" + fileName : null)
                    .medicalFolder(folder)
                    .build();

            return surgeryRepository.save(surgery);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    public Vaccination addVaccinationRecord(Long folderId, String vaccineName, int doseNumber, String manufacturer, LocalDate vaccinationDate) {
        MedicalFolder folder = medicalFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        Vaccination vaccination = Vaccination.builder()
                .vaccineName(vaccineName)
                .doseNumber(doseNumber)
                .manufacturer(manufacturer)
                .vaccinationDate(vaccinationDate)
                .medicalFolder(folder)
                .build();

        return vaccinationRepository.save(vaccination);
    }
}
