package com.amn.repository;

import com.amn.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findTopByUserIdOrderByExpirationDesc(Long userId);

    Optional<OTP> findTopByPatientIdOrderByExpirationDesc(Long patientId);

}
