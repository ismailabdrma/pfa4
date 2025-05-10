import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from 'src/app/services/admin.service';
import { UserDTO } from 'src/app/models/user.dto';
import { DoctorDTO } from 'src/app/models/doctor.dto';
import { PharmacistDTO } from 'src/app/models/pharmacist.dto';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  patients: UserDTO[] = [];
  doctors: DoctorDTO[] = [];
  pharmacists: PharmacistDTO[] = [];
  errorMessage = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadPatients();
    this.loadDoctors();
    this.loadPharmacists();
  }

  /**
   * Load all patients
   */
  loadPatients(): void {
    this.adminService.getAllPatients().subscribe({
      next: (data) => {
        this.patients = data;
        console.log('Loaded patients:', data);
      },
      error: (err) => {
        console.error('Error loading patients:', err);
        this.errorMessage = err.error?.message || 'Error loading patients.';
      },
    });
  }

  /**
   * Load all doctors
   */
  loadDoctors(): void {
    this.adminService.getAllDoctors().subscribe({
      next: (data) => {
        this.doctors = data;
        console.log('Loaded doctors:', data);
      },
      error: (err) => {
        console.error('Error loading doctors:', err);
        this.errorMessage = err.error?.message || 'Error loading doctors.';
      },
    });
  }

  /**
   * Load all pharmacists
   */
  loadPharmacists(): void {
    this.adminService.getAllPharmacists().subscribe({
      next: (data) => {
        this.pharmacists = data;
        console.log('Loaded pharmacists:', data);
      },
      error: (err) => {
        console.error('Error loading pharmacists:', err);
        this.errorMessage = err.error?.message || 'Error loading pharmacists.';
      },
    });
  }

  /**
   * Approve User
   */
  approveUser(userId: number): void {
    console.log('Approving user ID:', userId); // Add this
    this.adminService.approveUser(userId).subscribe({
      next: () => {
        console.log('Approval successful, reloading data...');
        this.loadDoctors();
        this.loadPharmacists();
        alert('User approved successfully.');
      },
      error: (err) => {
        console.error('Error approving user:', err);
        // Check if there's a response message
        if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = 'Error approving user.';
        }
      }
    });
  }

  /**
   * Reject User
   */
  rejectUser(userId: number): void {
    this.adminService.rejectUser(userId).subscribe({
      next: () => {
        this.loadDoctors();
        this.loadPharmacists();
        alert('User rejected successfully.');
      },
      error: (err) => {
        console.error('Error rejecting user:', err);
        this.errorMessage = 'Error rejecting user.';
      }
    });
  }

  /**
   * Suspend User (Doctor/Pharmacist)
   */
  suspendUser(userId: number): void {
    this.adminService.suspendUser(userId).subscribe({
      next: () => {
        this.loadDoctors();
        this.loadPharmacists();
        alert('User suspended successfully.');
      },
      error: (err) => {
        console.error('Error suspending user:', err);
        this.errorMessage = 'Error suspending user.';
      }
    });
  }

  /**
   * Delete User
   */
  deleteUser(userId: number): void {
    this.adminService.deleteUser(userId).subscribe({
      next: () => {
        this.loadPatients();
        this.loadDoctors();
        this.loadPharmacists();
        alert('User deleted successfully.');
      },
      error: (err) => {
        console.error('Error deleting user:', err);
        this.errorMessage = 'Error deleting user.';
      }
    });
  }
}
