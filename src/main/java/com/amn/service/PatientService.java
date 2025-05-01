package com.amn.service;

import com.amn.entity.MedicalFolder;
import com.amn.entity.OTP;
import com.amn.entity.Patient;
import com.amn.entity.enums.OTPChannel;
import com.amn.entity.enums.OTPStatus;
import com.amn.repository.MedicalFolderRepository;
import com.amn.repository.OTPRepository;
import com.amn.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalFolderRepository medicalFolderRepository;
    private final OTPRepository otpRepository;
   // private final PasswordEncoder passwordEncoder;

    // Register Patient
    public Patient registerPatient(Patient patient) {
        //patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        Patient saved = patientRepository.save(patient);

        // Create empty MedicalFolder for the patient
        MedicalFolder document = new MedicalFolder();
        document.setCreationDate(LocalDate.now());
        document.setDescription("Medical archive for " + saved.getFullName());
        document.setPatient(saved); // relation
        medicalFolderRepository.save(document);

        return saved;
    }

    // Login Patient
   // public Optional<Patient> login(String email, String password) {
      //  Optional<Patient> patientOpt = patientRepository.findByEmail(email);
       // if (patientOpt.isPresent() && passwordEncoder.matches(password, patientOpt.get().getPassword())) {
           // return patientOpt;
        //}
       // return Optional.empty();
   // }

    // View own MedicalFolder
    public Optional<MedicalFolder> viewMedicalDocument(Long patientId) {
        return medicalFolderRepository.findByPatientId(patientId);
    }

    // Generate OTP for patient
    public OTP generateOtpForPatient(Long patientId, OTPChannel channel) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        OTP otp = OTP.builder()
                .code(code)
                .channel(channel)
                .expiration(LocalDateTime.now().plusMinutes(5))
                .status(OTPStatus.PENDING)
                .patient(patient)
                .build();

        return otpRepository.save(otp);
    }

    // Verify OTP
    public boolean verifyOtp(Long patientId, String code) {
        OTP otp = otpRepository.findTopByPatientIdOrderByExpirationDesc(patientId)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otp.getCode().equals(code) && otp.getExpiration().isAfter(LocalDateTime.now())) {
            otp.setStatus(OTPStatus.VERIFIED);
            otpRepository.save(otp);
            return true;
        } else {
            otp.setStatus(OTPStatus.EXPIRED);
            otpRepository.save(otp);
            return false;
        }
    }
}
