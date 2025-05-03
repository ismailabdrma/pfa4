package com.amn.service;

import com.amn.dto.PatientProfileDTO;
import com.amn.entity.*;
import com.amn.entity.enums.OTPStatus;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("MedicalFolder not found"));

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
                .orElseThrow(() -> new RuntimeException("Patient not found or name does not match"));

        return medicalFolderRepository.findByPatientId(patient.getId())
                .orElseGet(() -> {
                    MedicalFolder folder = new MedicalFolder();
                    folder.setPatient(patient);
                    return medicalFolderRepository.save(folder);
                });
    }

    public List<Prescription> getPrescriptionsByCinAndName(String cin, String fullName) {
        Patient patient = patientRepository.findAllByCin(cin).stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(fullName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found or name does not match"));

        return prescriptionRepository.findAllByPatientId(patient.getId());
    }

    public MedicalFolder createMedicalFolder(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (medicalFolderRepository.findByPatientId(patientId).isPresent()) {
            throw new RuntimeException("Medical folder already exists for this patient.");
        }

        MedicalFolder folder = new MedicalFolder();
        folder.setPatient(patient);
        return medicalFolderRepository.save(folder);
    }

    public Long getPatientIdByCinAndName(String cin, String fullName) {
        return patientRepository.findAllByCin(cin).stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(fullName))
                .findFirst()
                .map(Patient::getId)
                .orElseThrow(() -> new RuntimeException("Patient not found or name mismatch"));
    }

    public MedicalFolder createMedicalFolderWithDetails(Long patientId, MedicalFolder folderData) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (medicalFolderRepository.findByPatientId(patientId).isPresent()) {
            throw new RuntimeException("Medical folder already exists.");
        }

        folderData.setPatient(patient);
        return medicalFolderRepository.save(folderData);
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
                .orElseThrow(() -> new RuntimeException("Patient not found or name does not match"));

        MedicalFolder folder = medicalFolderRepository.findByPatientId(patient.getId())
                .orElseThrow(() -> new RuntimeException("Medical folder not found"));

        return PatientProfileDTO.builder()
                .fullName(patient.getFullName())
                .cin(patient.getCin())
                .bloodType(patient.getBloodType())
                .birthDate(patient.getBirthDate() != null ? patient.getBirthDate().toString() : null)
                .emergencyContact(patient.getEmergencyContact())
                .allergies(patient.getAllergies())
                .chronicDiseases(patient.getChronicDiseases())
                .hasHeartProblem(patient.isHasHeartProblem())
                .hasSurgery(patient.isHasSurgery())
                .medicalRecords(folder.getMedicalRecords())
                .vaccinations(folder.getVaccinations())
                .visitLogs(folder.getVisitLogs())
                .scans(folder.getScans())
                .surgeries(folder.getSurgeries())
                .build();
    }
}
