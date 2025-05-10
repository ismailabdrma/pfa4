package com.amn.repository;

import com.amn.dto.PrescriptionDTO;
import com.amn.entity.Medication;
import com.amn.entity.Prescription;
import com.amn.entity.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient_Id(Long patientId);
    List<Prescription> findByPrescribingDoctorId(Long doctorId);
    List<Prescription> findByDispensingPharmacistId(Long pharmacistId);


    List<Prescription> findAllByPatientId(Long patientId); // ✅ Correct


    Optional<Prescription> findByMedicalRecordId(Long medicalRecordId);
    List<Prescription> findAllByPatientCinAndPatientFullName( String cin,  String fullName);

    List<Prescription> findByMedicalFolderId(Long folderId);
    @Query("SELECT p FROM Prescription p WHERE p.patient.cin = :cin AND LOWER(p.patient.fullName) = LOWER(:fullName)")
    List<Prescription> findByPatientCinAndName(@Param("cin") String cin, @Param("fullName") String fullName);

    /**
     * Find prescriptions for a patient by CIN, name and status
     */
    @Query("SELECT p FROM Prescription p WHERE p.patient.cin = :cin AND LOWER(p.patient.fullName) = LOWER(:fullName) AND p.status = :status")
    List<Prescription> findByPatientCinAndNameAndStatus(
            @Param("cin") String cin,
            @Param("fullName") String fullName,
            @Param("status") PrescriptionStatus status);
}
