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

    // ✅ Approve a doctor
    public DoctorDTO approveDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setStatus(AccountStatus.APPROVED);
        doctorRepository.save(doctor);

        return new DoctorDTO(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getEmail(),
                doctor.getMatricule(),
                doctor.getSpecialty(),
                doctor.getStatus().name()
        );
    }

    // ✅ Approve a pharmacist
    public PharmacistDTO approvePharmacist(Long pharmacistId) {
        Pharmacist pharmacist = pharmacistRepository.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));
        pharmacist.setStatus(AccountStatus.APPROVED);
        pharmacistRepository.save(pharmacist);

        return new PharmacistDTO(
                pharmacist.getId(),
                pharmacist.getFullName(),
                pharmacist.getEmail(),
                pharmacist.getMatricule(),
                pharmacist.getStatus().name()
        );
    }

    // ✅ Get all doctors
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> new DoctorDTO(
                        doctor.getId(),
                        doctor.getFullName(),
                        doctor.getEmail(),
                        doctor.getMatricule(),
                        doctor.getSpecialty(),
                        doctor.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Get all pharmacists
    public List<PharmacistDTO> getAllPharmacists() {
        return pharmacistRepository.findAll()
                .stream()
                .map(pharmacist -> new PharmacistDTO(
                        pharmacist.getId(),
                        pharmacist.getFullName(),
                        pharmacist.getEmail(),
                        pharmacist.getMatricule(),
                        pharmacist.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Get all patients (Users who are not doctors or pharmacists)
    // ✅ Get all patients (Users who are not doctors or pharmacists)
    // ✅ Get all patients
    // ✅ Fetch patients directly using the query
    public List<UserDTO> getAllPatients() {
        return patientRepository.findAllByRole(Role.PATIENT);
    }



    // ✅ Get all pending doctors
    public List<DoctorDTO> getPendingDoctors() {
        return doctorRepository.findAllByStatus(AccountStatus.PENDING)
                .stream()
                .map(doctor -> new DoctorDTO(
                        doctor.getId(),
                        doctor.getFullName(),
                        doctor.getEmail(),
                        doctor.getMatricule(),
                        doctor.getSpecialty(),
                        doctor.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Get all pending pharmacists
    public List<PharmacistDTO> getPendingPharmacists() {
        return pharmacistRepository.findAllByStatus(AccountStatus.PENDING)
                .stream()
                .map(pharmacist -> new PharmacistDTO(
                        pharmacist.getId(),
                        pharmacist.getFullName(),
                        pharmacist.getEmail(),
                        pharmacist.getMatricule(),
                        pharmacist.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Suspend User (Only Doctor and Pharmacist can be suspended)
    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        switch (user.getRole().name()) {
            case "DOCTOR":
                Doctor doctor = doctorRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Doctor not found"));
                doctor.setStatus(AccountStatus.SUSPENDED);
                doctorRepository.save(doctor);
                break;

            case "PHARMACIST":
                Pharmacist pharmacist = pharmacistRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Pharmacist not found"));
                pharmacist.setStatus(AccountStatus.SUSPENDED);
                pharmacistRepository.save(pharmacist);
                break;

            default:
                throw new RuntimeException("Only doctors and pharmacists can be suspended.");
        }
    }

    // ✅ Delete User (Only Doctor and Pharmacist can be deleted)
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        switch (user.getRole().name()) {
            case "DOCTOR":
                doctorRepository.deleteById(userId);
                break;

            case "PHARMACIST":
                pharmacistRepository.deleteById(userId);
                break;

            default:
                throw new RuntimeException("Only doctors and pharmacists can be deleted.");
        }
    }

}
