package com.amn.repository;

import com.amn.entity.MedicalFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalFolderRepository extends JpaRepository<MedicalFolder, Long> {

    Optional<MedicalFolder> findByPatientId(Long patientId);

}
