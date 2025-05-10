import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientService } from 'src/app/services/PatientService';
import { finalize } from 'rxjs';
import { RouterLink } from '@angular/router';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import {FormsModule} from '@angular/forms';

export interface PatientBasicInfoDTO {
  id: number;
  fullName: string;
  email: string;
  cin: string;
  address: string;
  phone: string;
  bloodType: string;
}

@Component({
  selector: 'app-patient-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  patientBasicInfo: PatientBasicInfoDTO | null = null;

  constructor(
    private patientService: PatientService,
    private localStorageService: LocalStorageService
  ) {}

  ngOnInit(): void {
    this.loadBasicInfo();
  }

  /**
   * ✅ Load basic info using the stored CIN
   */
  private loadBasicInfo(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const userCIN = this.localStorageService.getItem('user_cin');

    if (!userCIN) {
      this.errorMessage = 'User CIN not found. Please login again.';
      this.isLoading = false;
      console.warn(this.errorMessage);
      return;
    }

    console.log('Fetching basic info for CIN:', userCIN);

    this.patientService.getPatientBasicInfo(userCIN)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (data) => {
          console.log('Patient Basic Info:', data);
          this.patientBasicInfo = data;
        },
        error: (err) => {
          console.error('Error loading basic info:', err);
          this.errorMessage = 'Unable to load patient information.';
        }
      });
  }

  /**
   * ✅ Update basic information (Address, Email, Phone)
   */
  updateBasicInfo(): void {
    if (!this.patientBasicInfo) return;

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    console.log('Updating basic info:', this.patientBasicInfo);

    this.patientService.updatePatientBasicInfo(this.patientBasicInfo.cin, this.patientBasicInfo)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          console.log('Patient information updated successfully');
          this.successMessage = 'Information updated successfully.';
        },
        error: (err) => {
          console.error('Error updating information:', err);
          this.errorMessage = 'Unable to update information.';
        }
      });
  }
}
