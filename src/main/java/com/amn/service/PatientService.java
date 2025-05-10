package com.amn.service;

import com.amn.dto.*;
import com.amn.entity.*;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalFolderRepository medicalFolderRepository;
    private final AnalysisRepository analysisRepository;
    private final ScanRepository scanRepository;
    private final SurgeryRepository surgeryRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final VaccinationRepository vaccinationRepository;
    private final UserRepository userRepository;

    /**
     * ✅ Get Patient Profile by CIN
     */
    @Transactional(readOnly = true)
    public PatientProfileDTO getPatientProfileByCin(String cin) {
        // Get just the basic patient data first
        Patient patient = patientRepository.findByCin(cin)
                .orElseThrow(() -> new RuntimeException("Patient not found for CIN: " + cin));

        return buildPatientProfileFixed(patient);
    }

    /**
     * ✅ Get Patient Profile by Folder ID
     */
    @Transactional(readOnly = true)
    public PatientProfileDTO getPatientProfileByFolderId(Long folderId) {
        // Get just the folder first
        MedicalFolder folder = medicalFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Medical Folder not found for ID: " + folderId));

        // Then get the patient separately
        Patient patient = patientRepository.findById(folder.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("No patient associated with folder ID: " + folderId));

        return buildPatientProfileFixed(patient);
    }

    /**
     * ✅ Get Patient by Email
     */
    @Transactional(readOnly = true)
    public Patient getCurrentPatient(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found with email: " + email));
    }

    /**
     * ✅ Fetch Patient Profile by Email
     */
    @Transactional(readOnly = true)
    public PatientProfileDTO getPatientProfileByEmail(String email) {
        Patient patient = getCurrentPatient(email);
        return buildPatientProfileFixed(patient);
    }

    /**
     * ✅ FIXED: Build Patient Profile DTO to avoid MultipleBagFetchException
     */
    private PatientProfileDTO buildPatientProfileFixed(Patient patient) {
        // Start by building the basic profile without any collections
        PatientProfileDTO profile = PatientProfileDTO.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .cin(patient.getCin())
                .bloodType(patient.getBloodType())
                .birthDate(String.valueOf(patient.getBirthDate()))
                .emergencyContact(patient.getEmergencyContact())
                .allergies(patient.getAllergies())
                .chronicDiseases(patient.getChronicDiseases())
                .hasHeartProblem(patient.isHasHeartProblem())
                .hasSurgery(patient.isHasSurgery())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .build();

        // Get folder ID in a separate query
        Optional<MedicalFolder> folderOpt = medicalFolderRepository.findByPatientId(patient.getId());
        if (!folderOpt.isPresent()) {
            return profile; // Return just the basic profile if no folder exists
        }

        Long folderId = folderOpt.get().getId();
        profile.setMedicalFolderId(folderId);

        // Load each collection with separate transactions to avoid MultipleBagFetchException
        // This works because each method issues its own separate SQL query
        profile.setMedicalRecords(getMedicalRecords(folderId));
        profile.setPrescriptions(getPrescriptions(folderId));
        profile.setScans(getScans(folderId));
        profile.setAnalyses(getAnalyses(folderId));
        profile.setSurgeries(getSurgeries(folderId));
        profile.setVaccinations(getVaccinations(folderId));

        return profile;
    }

    /**
     * ✅ Fetch Medical Records by Folder ID
     */
    @Transactional(readOnly = true)
    public List<MedicalRecordDTO> getMedicalRecords(Long folderId) {
        return medicalRecordRepository.findByMedicalFolderId(folderId).stream()
                .map(MedicalRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Prescriptions by Folder ID
     */
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getPrescriptions(Long folderId) {
        return prescriptionRepository.findByMedicalFolderId(folderId).stream()
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Scans by Folder ID
     */
    @Transactional(readOnly = true)
    public List<ScanDTO> getScans(Long folderId) {
        return scanRepository.findByMedicalFolderId(folderId).stream()
                .map(ScanDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Analyses by Folder ID
     */
    @Transactional(readOnly = true)
    public List<AnalysisDTO> getAnalyses(Long folderId) {
        return analysisRepository.findByMedicalFolderId(folderId).stream()
                .map(AnalysisDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Surgeries by Folder ID
     */
    @Transactional(readOnly = true)
    public List<SurgeryDTO> getSurgeries(Long folderId) {
        return surgeryRepository.findByMedicalFolderId(folderId).stream()
                .map(SurgeryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Vaccinations by Folder ID
     */
    @Transactional(readOnly = true)
    public List<Vaccination> getVaccinations(Long folderId) {
        return vaccinationRepository.findByMedicalFolderId(folderId);
    }

    /**
     * ✅ Get Patient Basic Info
     */
    @Transactional(readOnly = true)
    public PatientBasicInfoDTO getPatientBasicInfo(String cin) {
        return patientRepository.findPatientBasicInfoByCin(cin);
    }

    /**
     * ✅ FIXED: Update Patient Basic Info (Address, Email, Phone)
     */
    @Transactional
    public void updatePatientBasicInfo(String cin, PatientBasicInfoDTO basicInfo) {
        // Use JPQL to get only the patient entity without collections
        Patient patient = patientRepository.findByCin(cin)
                .orElseThrow(() -> new RuntimeException("Patient not found for CIN: " + cin));

        // Get User separately without collections
        User user = userRepository.findById(patient.getId())
                .orElseThrow(() -> new RuntimeException("User not found for Patient ID: " + patient.getId()));

        // Update Address
        if (basicInfo.getAddress() != null) {
            user.setAddress(basicInfo.getAddress());
        }

        // Update Email
        if (basicInfo.getEmail() != null) {
            // Check if email exists but make sure we're not checking against the same user
            boolean emailExists = userRepository.existsByEmail(basicInfo.getEmail());
            if (emailExists && !basicInfo.getEmail().equals(user.getEmail())) {
                throw new RuntimeException("Email is already in use.");
            }
            user.setEmail(basicInfo.getEmail());
        }

        // Update Phone
        if (basicInfo.getPhone() != null) {
            user.setPhone(basicInfo.getPhone());
        }

        // Save the User entity
        userRepository.save(user);
    }
}