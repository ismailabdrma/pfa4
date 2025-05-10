package com.amn.repository;

import com.amn.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    /**
     * Find medications by name (case insensitive).
     */
    List<Medication> findByNameContainingIgnoreCase(String name);

    /**
     * Find medications by prescription ID.
     */
    List<Medication> findByPrescriptionId(Long prescriptionId);
}
