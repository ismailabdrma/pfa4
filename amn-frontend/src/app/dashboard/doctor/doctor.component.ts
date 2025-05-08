import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-doctor',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorComponent implements OnInit {
  doctorName = '';
  doctorEmail = '';
  cin = '';
  fullName = '';
  patientProfile: any = null;
  showManualForm = false;
  patientId: number | null = null;
  folderId: number | null = null;

  // Manual form data
  manualData = {
    bloodType: '',
    emergencyContact: '',
    allergies: '',
    chronicDiseases: '',
    hasHeartProblem: false,
    hasSurgery: false,
    birthDate: ''
  };

  // Vaccination form data
  vaccination = {
    vaccineName: '',
    doseNumber: 1,
    manufacturer: '',
    date: ''
  };

  // Upload data
  scanPayload = { title: '', description: '' };
  analysisPayload = { title: '', description: '' };
  surgeryPayload = { title: '', description: '' };

  scanFile: File | null = null;
  analysisFile: File | null = null;
  surgeryFile: File | null = null;

  constructor(private doctorService: DoctorService, private router: Router) {}

  ngOnInit(): void {
    this.loadDoctorProfile();
  }

  loadDoctorProfile(): void {
    this.doctorService.getCurrentDoctorProfile().subscribe({
      next: (doctor) => {
        this.doctorName = doctor.fullName || 'Docteur';
        this.doctorEmail = doctor.email || '';
      },
      error: () => {
        this.doctorName = 'Docteur';
        this.doctorEmail = '';
      }
    });
  }

  searchPatient(): void {
    this.doctorService.getFullPatientProfile(this.cin, this.fullName).subscribe({
      next: (profile) => {
        this.patientProfile = profile;
        this.patientId = profile.id;
        this.folderId = profile.medicalFolderId;

        this.showManualForm = !profile.bloodType || !profile.birthDate || !profile.emergencyContact;
      },
      error: (err) => {
        if (err.status === 404) {
          const confirmCreate = confirm("Patient introuvable. Créer un nouveau dossier ?");
          if (confirmCreate) {
            this.createPatientProfile();
          }
        } else {
          alert("Erreur lors de la recherche.");
        }
      }
    });
  }

  createPatientProfile(): void {
    this.doctorService.getPatientIdByCin(this.cin, this.fullName).subscribe({
      next: (id) => {
        this.patientId = id;
        this.showManualForm = true;
      },
      error: () => alert("Impossible de créer ou retrouver le patient.")
    });
  }

  submitManualFolder(): void {
    if (!this.patientId) return;

    this.doctorService.createOrUpdateMedicalFolder(this.cin, this.fullName, this.manualData).subscribe({
      next: () => {
        alert("Dossier mis à jour.");
        this.showManualForm = false;
        this.searchPatient();
      },
      error: () => alert("Erreur lors de la mise à jour.")
    });
  }

  addVaccination(): void {
    if (!this.folderId) return;

    this.doctorService.addVaccination(
      this.folderId,
      this.vaccination.vaccineName,
      this.vaccination.doseNumber,
      this.vaccination.manufacturer,
      this.vaccination.date
    ).subscribe({
      next: () => {
        alert("Vaccination ajoutée.");
        this.resetVaccinationForm();
        this.searchPatient();
      },
      error: () => alert("Erreur lors de l'ajout de la vaccination.")
    });
  }

  resetVaccinationForm(): void {
    this.vaccination = { vaccineName: '', doseNumber: 1, manufacturer: '', date: '' };
  }

  onScanFileChange(e: any): void {
    this.scanFile = e.target.files[0];
  }

  onAnalysisFileChange(e: any): void {
    this.analysisFile = e.target.files[0];
  }

  onSurgeryFileChange(e: any): void {
    this.surgeryFile = e.target.files[0];
  }

  uploadScan(): void {
    if (!this.folderId || !this.scanFile) return;

    const data = new FormData();
    data.append('file', this.scanFile);
    data.append('title', this.scanPayload.title);
    data.append('description', this.scanPayload.description);

    this.doctorService.uploadScan(data, this.folderId).subscribe({
      next: () => {
        alert("Scan ajouté.");
        this.resetScanForm();
        this.searchPatient();
      },
      error: () => alert("Erreur lors de l'ajout du scan.")
    });
  }

  uploadAnalysis(): void {
    if (!this.folderId || !this.analysisFile) return;

    const data = new FormData();
    data.append('file', this.analysisFile);
    data.append('title', this.analysisPayload.title);
    data.append('description', this.analysisPayload.description);

    this.doctorService.uploadAnalysis(data, this.folderId).subscribe({
      next: () => {
        alert("Analyse ajoutée.");
        this.resetAnalysisForm();
        this.searchPatient();
      },
      error: () => alert("Erreur lors de l'ajout de l'analyse.")
    });
  }

  uploadSurgery(): void {
    if (!this.folderId) return;

    const data = new FormData();
    data.append('title', this.surgeryPayload.title);
    data.append('description', this.surgeryPayload.description);
    if (this.surgeryFile) {
      data.append('file', this.surgeryFile);
    }

    this.doctorService.uploadSurgery(data, this.folderId).subscribe({
      next: () => {
        alert("Chirurgie ajoutée.");
        this.resetSurgeryForm();
        this.searchPatient();
      },
      error: () => alert("Erreur lors de l'ajout de la chirurgie.")
    });
  }

  resetScanForm(): void {
    this.scanPayload = { title: '', description: '' };
    this.scanFile = null;
  }

  resetAnalysisForm(): void {
    this.analysisPayload = { title: '', description: '' };
    this.analysisFile = null;
  }

  resetSurgeryForm(): void {
    this.surgeryPayload = { title: '', description: '' };
    this.surgeryFile = null;
  }

  viewScanDetails(id: number): void {
    this.router.navigate(['/dashboard/doctor/scan', id]);
  }

  viewAnalysisDetails(id: number): void {
    this.router.navigate(['/dashboard/doctor/analysis', id]);
  }

  viewSurgeryDetails(id: number): void {
    this.router.navigate(['/dashboard/doctor/surgery', id]);
  }

  viewConsultationDetails(id: number): void {
    this.router.navigate(['/dashboard/doctor/consultation', id]);
  }

  viewPrescriptionDetails(id: number): void {
    this.router.navigate(['/dashboard/doctor/prescription', id]);
  }
}
