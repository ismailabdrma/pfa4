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
  showEditProfileForm = false;
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

  // Consultation and prescription data
  consultation = {
    reason: '',
    notes: '',
    diagnosis: ''
  };

  medications: any[] = [];

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
      },
      error: () => {
        console.error('Erreur lors du chargement du profil du médecin.');
      }
    });
  }


  searchPatient(): void {
    this.doctorService.getFullPatientProfile(this.cin, this.fullName).subscribe({
      next: (profile) => {
        this.patientProfile = profile;
        this.patientId = profile.id;
        this.folderId = profile.medicalFolderId;

        // Initialize manual data with existing profile data
        if (profile) {
          this.manualData.bloodType = profile.bloodType || '';
          this.manualData.allergies = profile.allergies || '';
          this.manualData.chronicDiseases = profile.chronicDiseases || '';
          this.manualData.emergencyContact = profile.emergencyContact || '';
        }
      },
      error: () => {
        alert('Patient introuvable.');
      }
    });
  }

  editPatientProfile(): void {
    this.showEditProfileForm = true;
  }

  submitManualFolder(): void {
    if (!this.cin || !this.fullName || !this.folderId) return;

    this.doctorService.createOrUpdateMedicalFolder(this.cin, this.fullName, this.manualData).subscribe({
      next: () => {
        alert('Dossier médical mis à jour avec succès.');
        this.showEditProfileForm = false;
        this.searchPatient(); // Refresh patient data
      },
      error: () => {
        console.error('Erreur lors de la mise à jour du dossier médical.');
      }
    });
  }

  // File upload handlers
  onScanFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.scanFile = input.files ? input.files[0] : null;
  }

  onAnalysisFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.analysisFile = input.files ? input.files[0] : null;
  }

  onSurgeryFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.surgeryFile = input.files ? input.files[0] : null;
  }

  uploadScan(): void {
    if (!this.folderId || !this.scanFile) return;

    const formData = new FormData();
    formData.append('file', this.scanFile);
    formData.append('title', this.scanPayload.title);
    formData.append('description', this.scanPayload.description);

    this.doctorService.uploadScan(formData, this.folderId).subscribe({
      next: () => {
        alert('Scan ajouté.');
        this.resetScanForm();
        this.searchPatient(); // Refresh data
      },
      error: () => {
        console.error('Erreur lors de l\'ajout du scan.');
      }
    });
  }

  uploadAnalysis(): void {
    if (!this.folderId || !this.analysisFile) return;

    const formData = new FormData();
    formData.append('file', this.analysisFile);
    formData.append('title', this.analysisPayload.title);
    formData.append('description', this.analysisPayload.description);

    this.doctorService.uploadAnalysis(formData, this.folderId).subscribe({
      next: () => {
        alert('Analyse ajoutée.');
        this.resetAnalysisForm();
        this.searchPatient(); // Refresh data
      },
      error: () => {
        console.error('Erreur lors de l\'ajout de l\'analyse.');
      }
    });
  }

  uploadSurgery(): void {
    if (!this.folderId || !this.surgeryFile) return;

    const formData = new FormData();
    formData.append('file', this.surgeryFile);
    formData.append('title', this.surgeryPayload.title);
    formData.append('description', this.surgeryPayload.description);

    this.doctorService.uploadSurgery(formData, this.folderId).subscribe({
      next: () => {
        alert('Chirurgie ajoutée.');
        this.resetSurgeryForm();
        this.searchPatient(); // Refresh data
      },
      error: () => {
        console.error('Erreur lors de l\'ajout de la chirurgie.');
      }
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

  // Vaccination methods
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
        alert('Vaccination ajoutée.');
        this.vaccination = {
          vaccineName: '',
          doseNumber: 1,
          manufacturer: '',
          date: ''
        };
        this.searchPatient(); // Refresh data
      },
      error: () => {
        console.error('Erreur lors de l\'ajout de la vaccination.');
      }
    });
  }

  // Consultation and prescription methods
  createConsultation(): void {
    if (!this.patientId) return;

    const consultationData = {
      reason: this.consultation.reason,
      notes: this.consultation.notes,
      diagnosis: this.consultation.diagnosis
    };

    this.doctorService.createMedicalRecord(this.patientId, consultationData).subscribe({
      next: (record) => {
        alert('Consultation créée avec succès.');
        this.searchPatient(); // Refresh data
      },
      error: () => {
        console.error('Erreur lors de la création de la consultation.');
      }
    });
  }

  addMedication(): void {
    this.medications.push({
      name: '',
      dosage: '',
      period: ''
    });
  }

  removeMedication(index: number): void {
    this.medications.splice(index, 1);
  }

  submitPrescription(): void {
    if (!this.patientId || !this.patientProfile?.medicalRecords?.length) return;

    // Get the latest medical record ID
    const latestRecordId = this.patientProfile.medicalRecords[this.patientProfile.medicalRecords.length - 1].id;

    const prescriptionData = {
      medications: this.medications,
      notes: this.consultation.notes
    };

    this.doctorService.submitPrescriptionWithMedications(
      this.patientId,
      latestRecordId,
      prescriptionData
    ).subscribe({
      next: () => {
        alert('Ordonnance créée avec succès.');
        this.medications = [];
        this.consultation = {
          reason: '',
          notes: '',
          diagnosis: ''
        };
        this.searchPatient(); // Refresh data
      },
      error: () => {
        console.error('Erreur lors de la création de l\'ordonnance.');
      }
    });
  }

  // Navigation methods
  navigateToConsultation(): void {
    if (!this.patientProfile) {
      alert("Veuillez d'abord sélectionner un patient.");
      return;
    }
    this.router.navigate([
      '/dashboard/doctor/consultation-prescription',
      this.cin,
      this.fullName
    ]);
  }

  viewConsultationDetails(recordId: number): void {
    this.router.navigate(['/dashboard/doctor/consultation', recordId]);
  }

  viewPrescriptionDetails(prescriptionId: number): void {
    this.router.navigate(['/dashboard/doctor/prescription', prescriptionId]);
  }
}
