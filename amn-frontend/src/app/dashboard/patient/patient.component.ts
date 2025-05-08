import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PatientService } from 'src/app/services/PatientService';
import { PatientProfileDTO } from 'src/app/models/patient-profile.dto';

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {
  patientProfile: PatientProfileDTO | null = null;
  cin = '';
  errorMessage = '';

  constructor(private patientService: PatientService) {}

  ngOnInit(): void {}

  /**
   * Fetch patient profile by CIN
   */
  getPatientProfile(): void {
    if (!this.cin.trim()) {
      this.errorMessage = 'CIN requis';
      this.patientProfile = null;
      return;
    }

    this.patientService.getPatientProfile(this.cin).subscribe({
      next: (data: PatientProfileDTO) => {
        this.patientProfile = data;
        this.errorMessage = '';
      },
      error: () => {
        this.errorMessage = 'Aucun dossier trouvé pour ce CIN.';
        this.patientProfile = null;
      }
    });
  }
}
