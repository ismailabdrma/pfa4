package com.amn.controller;

import com.amn.dto.DoctorDTO;
import com.amn.dto.PharmacistDTO;
import com.amn.dto.UserDTO;
import com.amn.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * ✅ Approve User (Doctor/Pharmacist)
     */
    @PatchMapping("/users/approve/{id}")
    public ResponseEntity<Map<String, String>> approveUser(@PathVariable Long id) {
        adminService.approveUser(id);
        return ResponseEntity.ok(Map.of("message", "User approved successfully."));
    }

    /**
     * ✅ Reject User (Doctor/Pharmacist)
     */
    @PatchMapping("/users/reject/{id}")
    public ResponseEntity<Map<String, String>> rejectUser(@PathVariable Long id) {
        adminService.rejectUser(id);
        return ResponseEntity.ok(Map.of("message", "User rejected successfully."));
    }

    /**
     * ✅ Suspend User (Doctor/Pharmacist Only)
     */
    @PatchMapping("/users/suspend/{id}")
    public ResponseEntity<Map<String, String>> suspendUser(@PathVariable Long id) {
        adminService.suspendUser(id);
        return ResponseEntity.ok(Map.of("message", "User suspended successfully."));
    }

    /**
     * ✅ Delete User (Doctor/Pharmacist Only)
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully."));
    }

    /**
     * ✅ Get All Pending Doctors
     */
    @GetMapping("/users/pending-doctors")
    public ResponseEntity<List<DoctorDTO>> getPendingDoctors() {
        List<DoctorDTO> pendingDoctors = adminService.getPendingDoctors();
        return ResponseEntity.ok(pendingDoctors);
    }

    /**
     * ✅ Get All Pending Pharmacists
     */
    @GetMapping("/users/pending-pharmacists")
    public ResponseEntity<List<PharmacistDTO>> getPendingPharmacists() {
        List<PharmacistDTO> pendingPharmacists = adminService.getPendingPharmacists();
        return ResponseEntity.ok(pendingPharmacists);
    }

    /**
     * ✅ Get All Doctors
     */
    @GetMapping("/users/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = adminService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * ✅ Get All Pharmacists
     */
    @GetMapping("/users/pharmacists")
    public ResponseEntity<List<PharmacistDTO>> getAllPharmacists() {
        List<PharmacistDTO> pharmacists = adminService.getAllPharmacists();
        return ResponseEntity.ok(pharmacists);
    }

    /**
     * ✅ Get All Patients
     */
    @GetMapping("/users/patients")
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        List<UserDTO> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
}
