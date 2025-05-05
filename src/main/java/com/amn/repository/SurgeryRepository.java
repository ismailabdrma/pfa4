package com.amn.repository;

import com.amn.entity.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurgeryRepository extends JpaRepository<Surgery, Long> {
   List<Surgery> findByMedicalFolder_Id(Long medicalFolderId);
   List<Surgery> findByMedicalFolderId(Long folderId);

    List<Surgery> findAllByMedicalFolderId(Long id);
}
