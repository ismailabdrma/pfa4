package com.amn.repository;

import com.amn.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
List<Prescription> findByPatient(Long patientId);
    List<Prescription> findByPrescribingDoctorId(Long doctorId);
    List<Prescription> findByDispensingPharmacistId(Long pharmacistId);

}
