package com.amn.repository;

import com.amn.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByNameContainingIgnoreCase(String name);

}
