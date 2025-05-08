package com.amn.controller;

import com.amn.dto.DoctorDTO;
import com.amn.dto.PharmacistDTO;
import com.amn.dto.UserDTO;
import com.amn.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * ✅ Approve Doctor
     */
    @PostMapping("/approve-doctor/{id}")
    public ResponseEntity<DoctorDTO> approveDoctor(@PathVariable Long id) {
        DoctorDTO approvedDoctor = adminService.approveDoctor(id);
        return ResponseEntity.ok(approvedDoctor);
    }

    /**
     * ✅ Approve Pharmacist
     */
    @PostMapping("/approve-pharmacist/{id}")
    public ResponseEntity<PharmacistDTO> approvePharmacist(@PathVariable Long id) {
        PharmacistDTO approvedPharmacist = adminService.approvePharmacist(id);
        return ResponseEntity.ok(approvedPharmacist);
    }

    /**
     * ✅ Get Pending Doctors
     */
    @GetMapping("/pending-doctors")
    public ResponseEntity<List<DoctorDTO>> getPendingDoctors() {
        List<DoctorDTO> pendingDoctors = adminService.getPendingDoctors();
        return ResponseEntity.ok(pendingDoctors);
    }

    /**
     * ✅ Get Pending Pharmacists
     */
    @GetMapping("/pending-pharmacists")
    public ResponseEntity<List<PharmacistDTO>> getPendingPharmacists() {
        List<PharmacistDTO> pendingPharmacists = adminService.getPendingPharmacists();
        return ResponseEntity.ok(pendingPharmacists);
    }

    /**
     * ✅ Get All Doctors
     */
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = adminService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * ✅ Get All Pharmacists
     */
    @GetMapping("/pharmacists")
    public ResponseEntity<List<PharmacistDTO>> getAllPharmacists() {
        List<PharmacistDTO> pharmacists = adminService.getAllPharmacists();
        return ResponseEntity.ok(pharmacists);
    }

    /**
     * ✅ Get All Users (Patients Only)
     */
    // ✅ Get All Patients
    @GetMapping("/patients")
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        List<UserDTO> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients);
    }


    /**
     * ✅ Suspend User (Doctor/Pharmacist Only)
     */
    @PatchMapping("/suspend/{id}")
    public ResponseEntity<String> suspendUser(@PathVariable Long id) {
        adminService.suspendUser(id);
        return ResponseEntity.ok("User suspended successfully.");
    }

    /**
     * ✅ Delete User (Doctor/Pharmacist Only)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

}
