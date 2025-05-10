package com.amn.service;

import com.amn.dto.DoctorDTO;
import com.amn.dto.PharmacistDTO;
import com.amn.dto.UserDTO;
import com.amn.entity.Doctor;
import com.amn.entity.Pharmacist;
import com.amn.entity.User;
import com.amn.entity.enums.AccountStatus;
import com.amn.entity.enums.Role;
import com.amn.repository.DoctorRepository;
import com.amn.repository.PatientRepository;
import com.amn.repository.PharmacistRepository;
import com.amn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DoctorRepository doctorRepository;
    private final PharmacistRepository pharmacistRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    // ✅ Approve User
    public void approveUser(Long userId) {
        User user = findUserById(userId);

        switch (user.getRole()) {
            case DOCTOR -> {
                Doctor doctor = findDoctorById(userId);
                doctor.setStatus(AccountStatus.APPROVED);
                doctorRepository.save(doctor);
            }
            case PHARMACIST -> {
                Pharmacist pharmacist = findPharmacistById(userId);
                pharmacist.setStatus(AccountStatus.APPROVED);
                pharmacistRepository.save(pharmacist);
            }
            default -> throw new RuntimeException("Only doctors and pharmacists can be approved.");
        }
    }

    // ✅ Get All Doctors
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDoctorDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get All Pharmacists
    public List<PharmacistDTO> getAllPharmacists() {
        return pharmacistRepository.findAll().stream()
                .map(this::mapToPharmacistDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get All Patients
    public List<UserDTO> getAllPatients() {
        return patientRepository.findAllByRole(Role.PATIENT);
    }

    // ✅ Get Pending Doctors
    public List<DoctorDTO> getPendingDoctors() {
        return doctorRepository.findAllByStatus(AccountStatus.PENDING).stream()
                .map(this::mapToDoctorDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get Pending Pharmacists
    public List<PharmacistDTO> getPendingPharmacists() {
        return pharmacistRepository.findAllByStatus(AccountStatus.PENDING).stream()
                .map(this::mapToPharmacistDTO)
                .collect(Collectors.toList());
    }

    // ✅ Suspend User
    public void suspendUser(Long userId) {
        User user = findUserById(userId);

        switch (user.getRole()) {
            case DOCTOR -> {
                Doctor doctor = findDoctorById(userId);
                doctor.setStatus(AccountStatus.SUSPENDED);
                doctorRepository.save(doctor);
            }
            case PHARMACIST -> {
                Pharmacist pharmacist = findPharmacistById(userId);
                pharmacist.setStatus(AccountStatus.SUSPENDED);
                pharmacistRepository.save(pharmacist);
            }
            default -> throw new RuntimeException("Only doctors and pharmacists can be suspended.");
        }
    }

    // ✅ Delete User
    public void deleteUser(Long userId) {
        User user = findUserById(userId);

        switch (user.getRole()) {
            case DOCTOR -> doctorRepository.deleteById(userId);
            case PHARMACIST -> pharmacistRepository.deleteById(userId);
            default -> throw new RuntimeException("Only doctors and pharmacists can be deleted.");
        }
    }

    // ✅ Reject User
    public void rejectUser(Long userId) {
        User user = findUserById(userId);

        switch (user.getRole()) {
            case DOCTOR -> {
                Doctor doctor = findDoctorById(userId);
                doctor.setStatus(AccountStatus.REJECTED);
                doctorRepository.save(doctor);
            }
            case PHARMACIST -> {
                Pharmacist pharmacist = findPharmacistById(userId);
                pharmacist.setStatus(AccountStatus.REJECTED);
                pharmacistRepository.save(pharmacist);
            }
            default -> throw new RuntimeException("Only doctors and pharmacists can be rejected.");
        }
    }

    // ✅ Helper Methods
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Doctor findDoctorById(Long userId) {
        return doctorRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    private Pharmacist findPharmacistById(Long userId) {
        return pharmacistRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));
    }

    private DoctorDTO mapToDoctorDTO(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getEmail(),
                doctor.getMatricule(),
                doctor.getSpecialty(),
                doctor.getStatus()
        );
    }

    private PharmacistDTO mapToPharmacistDTO(Pharmacist pharmacist) {
        return new PharmacistDTO(
                pharmacist.getId(),
                pharmacist.getFullName(),
                pharmacist.getEmail(),
                pharmacist.getMatricule(),
                pharmacist.getStatus()
        );
    }
}
