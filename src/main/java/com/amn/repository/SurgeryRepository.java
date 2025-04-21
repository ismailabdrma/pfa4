package com.amn.repository;

import com.amn.entity.Surgery;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurgeryRepository extends JpaRepository<Surgery, Long> {
   List<Surgery> findByMedicalRecordId(Long medicalRecordId);
}
