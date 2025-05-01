package com.amn.service;

import com.amn.entity.Doctor;
import com.amn.entity.Patient;
import com.amn.entity.Pharmacist;
import com.amn.entity.enums.AccountStatus;
import com.amn.repository.DoctorRepository;
import com.amn.repository.PharmacistRepository;
import com.amn.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DoctorRepository doctorRepository;
    private final PharmacistRepository pharmacistRepository;
    private final PatientRepository patientRepository;

    public Doctor approveDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setStatus(AccountStatus.APPROVED);
        return doctorRepository.save(doctor);
    }

    public Pharmacist approvePharmacist(Long pharmacistId) {
        Pharmacist pharmacist = pharmacistRepository.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));
        pharmacist.setStatus(AccountStatus.APPROVED);
        return pharmacistRepository.save(pharmacist);
    }

    public void rejectRegistration(Long userId) {
        // Try deleting from doctors first
        if (doctorRepository.existsById(userId)) {
            doctorRepository.deleteById(userId);
            return;
        }
        // Try pharmacists
        if (pharmacistRepository.existsById(userId)) {
            pharmacistRepository.deleteById(userId);
            return;
        }
        // Try patients
        if (patientRepository.existsById(userId)) {
            patientRepository.deleteById(userId);
            return;
        }
        throw new RuntimeException("User not found");
    }

    public void deleteUser(Long userId) {
        rejectRegistration(userId); // same as reject
    }
}
