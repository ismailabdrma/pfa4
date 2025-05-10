package com.amn.repository;

import com.amn.dto.PatientBasicInfoDTO;
import com.amn.dto.UserDTO;
import com.amn.entity.Patient;
import com.amn.entity.enums.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    @EntityGraph(attributePaths = {
            "medicalFolder.medicalRecords",
            "medicalFolder.prescriptions.medications",
            "medicalFolder.scans",
            "medicalFolder.analyses",
            "medicalFolder.surgeries"
    })
    Optional<Patient> findByCin(String cin);
    List<Patient> findAllByCin(String cin);
    @Query("SELECT new com.amn.dto.UserDTO(u.id, u.fullName, u.email, u.role) FROM Patient u WHERE u.role = :role")
    List<UserDTO> findAllByRole(@Param("role") Role role);
    @Query("SELECT new com.amn.dto.PatientBasicInfoDTO(p.id, p.fullName, p.email, p.cin, p.address, p.phone, p.bloodType) FROM Patient p WHERE p.cin = :cin")
    PatientBasicInfoDTO findPatientBasicInfoByCin(@Param("cin") String cin);

}
