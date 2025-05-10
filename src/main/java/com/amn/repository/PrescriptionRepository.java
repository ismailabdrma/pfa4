package com.amn.repository;

import com.amn.dto.PrescriptionDTO;
import com.amn.entity.Medication;
import com.amn.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
