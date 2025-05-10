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
    public PatientProfileDTO getPatientProfileByCin(String cin) {
        Patient patient = patientRepository.findByCin(cin)
                .orElseThrow(() -> new RuntimeException("Patient not found for CIN: " + cin));

        return buildPatientProfile(patient);
    }

    /**
     * ✅ Get Patient Profile by Folder ID
     */
    public PatientProfileDTO getPatientProfileByFolderId(Long folderId) {
        MedicalFolder folder = medicalFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Medical Folder not found for ID: " + folderId));

        Patient patient = folder.getPatient();
        if (patient == null) {
            throw new RuntimeException("No patient associated with folder ID: " + folderId);
        }

        return buildPatientProfile(patient);
    }

    /**
     * ✅ Build Patient Profile DTO
     */
    private PatientProfileDTO buildPatientProfile(Patient patient) {
        Optional<MedicalFolder> optionalFolder = medicalFolderRepository.findByPatientId(patient.getId());
        Long folderId = optionalFolder.map(MedicalFolder::getId).orElse(null);

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
                .medicalFolderId(folderId)
                .build();

        if (folderId != null) {
            profile.setMedicalRecords(getMedicalRecords(folderId));
            profile.setPrescriptions(getPrescriptions(folderId));
            profile.setScans(getScans(folderId));
            profile.setAnalyses(getAnalyses(folderId));
            profile.setSurgeries(getSurgeries(folderId));
            profile.setVaccinations(getVaccinations(folderId));
        }

        return profile;
    }

    /**
     * ✅ Fetch Medical Records by Folder ID
     */
    public List<MedicalRecordDTO> getMedicalRecords(Long folderId) {
        return medicalRecordRepository.findByMedicalFolderId(folderId).stream()
                .map(MedicalRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Prescriptions by Folder ID
     */
    public List<PrescriptionDTO> getPrescriptions(Long folderId) {
        return prescriptionRepository.findByMedicalFolderId(folderId).stream()
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Scans by Folder ID
     */
    public List<ScanDTO> getScans(Long folderId) {
        return scanRepository.findByMedicalFolderId(folderId).stream()
                .map(ScanDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Analyses by Folder ID
     */
    public List<AnalysisDTO> getAnalyses(Long folderId) {
        return analysisRepository.findByMedicalFolderId(folderId).stream()
                .map(AnalysisDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Surgeries by Folder ID
     */
    public List<SurgeryDTO> getSurgeries(Long folderId) {
        return surgeryRepository.findByMedicalFolderId(folderId).stream()
                .map(SurgeryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch Vaccinations by Folder ID
     */
    public List<Vaccination> getVaccinations(Long folderId) {
        return vaccinationRepository.findByMedicalFolderId(folderId);
    }

    /**
     * ✅ Get Patient by Email
     */
    public Patient getCurrentPatient(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found with email: " + email));
    }

    /**
     * ✅ Fetch Patient Profile by Email
     */
    public PatientProfileDTO getPatientProfileByEmail(String email) {
        Patient patient = getCurrentPatient(email);
        return buildPatientProfile(patient);
    }
    public PatientBasicInfoDTO getPatientBasicInfo(String cin) {
        return patientRepository.findPatientBasicInfoByCin(cin);
    }
    /**
     * ✅ Update Patient Basic Info (Address, Email, Phone)
     */
    @Transactional
    public void updatePatientBasicInfo(String cin, PatientBasicInfoDTO basicInfo) {
        Patient patient = patientRepository.findByCin(cin)
                .orElseThrow(() -> new RuntimeException("Patient not found for CIN: " + cin));

        // Get the User instance linked to this patient
        User user = userRepository.findById(patient.getId())
                .orElseThrow(() -> new RuntimeException("User not found for Patient ID: " + patient.getId()));

        if (basicInfo.getAddress() != null) {
            user.setAddress(basicInfo.getAddress());
        }
        if (basicInfo.getEmail() != null) {
            user.setEmail(basicInfo.getEmail());
        }
        if (basicInfo.getPhone() != null) {
            user.setPhone(basicInfo.getPhone());
        }

        // Save the User entity
        userRepository.save(user);
    }



}



















