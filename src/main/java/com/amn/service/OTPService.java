package com.amn.service;

import com.amn.entity.OTP;
import com.amn.entity.Patient;
import com.amn.entity.enums.OTPChannel;
import com.amn.entity.enums.OTPStatus;
import com.amn.repository.OTPRepository;
import com.amn.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final OTPRepository otpRepository;
    private final PatientRepository patientRepository;

    // Generate a new OTP
    public OTP generateOtp(Long patientId, OTPChannel channel) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        OTP otp = OTP.builder()
                .code(code)
                .expiration(LocalDateTime.now().plusMinutes(5))
                .status(OTPStatus.PENDING)
                .channel(channel)
                .patient(patient)
                .build();

        return otpRepository.save(otp);
    }

    // Verify an OTP
    public boolean verifyOtp(Long patientId, String code) {
        Optional<OTP> latestOtpOpt = otpRepository.findTopByPatientIdOrderByExpirationDesc(patientId);

        if (latestOtpOpt.isEmpty()) {
            throw new RuntimeException("OTP not found");
        }

        OTP otp = latestOtpOpt.get();

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
