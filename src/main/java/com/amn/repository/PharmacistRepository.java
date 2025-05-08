package com.amn.repository;

import com.amn.entity.Pharmacist;
import com.amn.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    Optional<Pharmacist> findByEmail(String email);
    Optional<Pharmacist> findByMatricule(String matricule);

    List<Pharmacist> findAllByStatus(AccountStatus status);
}
