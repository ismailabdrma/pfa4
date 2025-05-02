package com.amn.controller;

import com.amn.entity.Doctor;
import com.amn.entity.Pharmacist;
import com.amn.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ✅ Approve a doctor
    @PostMapping("/approve-doctor/{id}")
    public ResponseEntity<Doctor> approveDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approveDoctor(id));
    }

    // ✅ Approve a pharmacist
    @PostMapping("/approve-pharmacist/{id}")
    public ResponseEntity<Pharmacist> approvePharmacist(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approvePharmacist(id));
    }

    // ✅ Reject (delete) a registration
    @DeleteMapping("/reject/{id}")
    public ResponseEntity<String> rejectRegistration(@PathVariable Long id) {
        adminService.rejectRegistration(id);
        return ResponseEntity.ok("Registration rejected and user deleted.");
    }

    // ✅ Delete user (same as reject)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
