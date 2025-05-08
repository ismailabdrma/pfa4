package com.amn.repository;

import com.amn.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord>findByMedicalFolderId(Long medicalDocumentId);
    List<MedicalRecord> findAllByMedicalFolderId(Long folderId);
}
