import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PatientService, PatientBasicInfoDTO, PatientProfileDTO } from 'src/app/services/PatientService';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { finalize } from 'rxjs';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-patient-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  // Patient data
  patientBasicInfo: PatientBasicInfoDTO | null = null;
  patientProfile: PatientProfileDTO | null = null;

  // Edit mode
  isEditMode: boolean = false;
  editableInfo: PatientBasicInfoDTO | null = null;

  // Active tab for medical documents
  activeDocTab: string = 'scans';

  constructor(
    private patientService: PatientService,
    private localStorageService: LocalStorageService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  /**
   * Load user data using both CIN and email from localStorage
   */
  private loadUserData(): void {
    this.loadBasicInfo();
    this.loadPatientProfile();
  }

  /**
   * Load basic info using the stored CIN
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
        next: (data: any) => {
          console.log('Patient Basic Info:', data);
          this.patientBasicInfo = data;
          this.editableInfo = { ...data }; // Create a copy for editing
        },
        error: (err: any) => {
          console.error('Error loading basic info:', err);
          this.errorMessage = 'Unable to load patient information.';
        }
      });
  }

  /**
   * Load complete patient profile using email from localStorage
   */
  private loadPatientProfile(): void {
    const userEmail = this.localStorageService.getItem('user_email');

    if (!userEmail) {
      console.warn('User email not found. Cannot load full profile.');
      return;
    }

    this.isLoading = true;
    console.log('Fetching full profile for email:', userEmail);

    this.patientService.getPatientProfileByEmail(userEmail)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (data: any) => {
          console.log('Patient Profile loaded:', data);
          this.patientProfile = data;
        },
        error: (err: any) => {
          console.error('Error loading patient profile:', err);
          // Don't set error message here if basic info loaded successfully
        }
      });
  }

  /**
   * Switch to edit mode for patient info
   */
  enableEditMode(): void {
    if (this.patientBasicInfo) {
      this.editableInfo = { ...this.patientBasicInfo };
      this.isEditMode = true;
    }
  }

  /**
   * Cancel edit mode
   */
  cancelEdit(): void {
    this.isEditMode = false;
    // Reset editable info to original values
    if (this.patientBasicInfo) {
      this.editableInfo = { ...this.patientBasicInfo };
    }
  }

  /**
   * Update basic information (Address, Email, Phone)
   */
  updateBasicInfo(): void {
    if (!this.patientBasicInfo || !this.editableInfo) return;

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    console.log('Updating basic info:', this.editableInfo);

    this.patientService.updatePatientBasicInfo(this.editableInfo.cin, this.editableInfo)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          console.log('Patient information updated successfully');
          this.successMessage = 'Information updated successfully.';

          // Update the stored info with editable values
          this.patientBasicInfo = { ...this.editableInfo! };

          // Also update profile data if available
          if (this.patientProfile && this.editableInfo) {
            this.patientProfile.email = this.editableInfo.email;
            this.patientProfile.phone = this.editableInfo.phone;
            this.patientProfile.address = this.editableInfo.address;
          }

          // Exit edit mode
          this.isEditMode = false;
        },
        error: (err: any) => {
          console.error('Error updating information:', err);
          this.errorMessage = 'Unable to update information.';
        }
      });
  }

  /**
   * Set active document tab
   */
  setActiveDocTab(tabId: string): void {
    this.activeDocTab = tabId;
  }

  /**
   * View medical file
   */
  viewFile(url: string): void {
    if (!url) return;

    // Add server base URL if not already present
    if (!url.startsWith('http')) {
      url = 'http://localhost:8080' + url;
    }

    window.open(url, '_blank');
  }

  /**
   * Format date for display
   */
  formatDate(dateString?: string): string {
    if (!dateString) return 'N/A';

    try {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    } catch (e) {
      return dateString;
    }
  }

  /**
   * Count items in a collection
   */
  getItemCount(collection: any[] | undefined): number {
    return collection ? collection.length : 0;
  }
}
