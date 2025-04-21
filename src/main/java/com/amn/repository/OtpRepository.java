package com.amn.repository;

import com.amn.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findTopByPatientIdOrderByExpirationDesc(Long patientId);
    Optional<OTP> findByCodeAndPatientId(String code, Long patientId);
}