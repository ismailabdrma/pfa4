package com.amn.repository;

import com.amn.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByMedicalFolderId(Long medicalDocumentId);
}
