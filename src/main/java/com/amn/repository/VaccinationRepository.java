package com.amn.repository;

import com.amn.entity.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    List<Vaccination> findByMedicalRecordId(Long medicalRecordId);
}
