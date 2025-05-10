package com.amn.repository;

import com.amn.entity.Doctor;
import com.amn.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByMatricule(String matricule);

    List<Doctor> findAllByStatus(AccountStatus status);
    Optional<Doctor> findById(Long id);



}
