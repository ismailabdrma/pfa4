import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './patient-profile.component.html'
})
export class PatientProfileComponent {
  cin = '';
  fullName = '';
  showManualForm = false;
  patientId: number | null = null;
  folderId: number | null = null;

  patientProfile: any = null;

  manualData = {
    bloodType: '',
    emergencyContact: '',
    allergies: '',
    chronicDiseases: '',
    hasHeartProblem: false,
    hasSurgery: false,
    birthDate: ''
  };

  medicalRecord = {
    reason: '',
    diagnosis: '',
    notes: ''
  };

  prescription = {
    medicines: '',
    instructions: ''
  };

  scanPayload = { title: '', description: '' };
  analysisPayload = { title: '', description: '' };
  surgeryPayload = { title: '', description: '' };

  scanFile: File | null = null;
  analysisFile: File | null = null;
  surgeryFile: File | null = null;

  constructor(private doctorService: DoctorService, private router: Router) {}

  searchPatient() {
    this.doctorService.getFullPatientProfile(this.cin, this.fullName).subscribe({
      next: (profile) => {
        this.patientProfile = profile;
        this.patientId = profile.id;
        this.folderId = profile.medicalFolderId || profile.folderId || null;
        this.showManualForm = !profile.bloodType || !profile.birthDate || !profile.emergencyContact;
      },
      error: (err) => {
        if (err.status === 404) {
          const confirmCreate = confirm("Patient introuvable. Créer un nouveau dossier ?");
          if (confirmCreate) {
            this.doctorService.getPatientIdByCin(this.cin, this.fullName).subscribe({
              next: (id) => {
                this.patientId = id;
                this.showManualForm = true;
              },
              error: () => alert("Impossible de trouver/créer l'utilisateur.")
            });
          }
        } else {
          alert("Erreur lors de la recherche du patient.");
        }
      }
    });
  }

  submitManualFolder() {
    if (!this.patientId) return;
    this.doctorService.createOrUpdateMedicalFolder(this.cin, this.fullName, this.manualData).subscribe({
      next: () => {
        alert("Dossier mis à jour.");
        this.showManualForm = false;
        this.searchPatient();
      },
      error: () => alert("Erreur mise à jour du dossier")
    });
  }

  createMedicalRecord() {
    if (!this.patientId) return;
    this.doctorService.createMedicalRecord(this.patientId, this.medicalRecord).subscribe({
      next: () => {
        alert('Consultation enregistrée');
        this.searchPatient();
      },
      error: () => alert('Erreur ajout consultation')
    });
  }

  writePrescription() {
    if (!this.patientId) return;
    this.doctorService.writePrescription(this.patientId, this.prescription).subscribe({
      next: () => {
        alert('Prescription enregistrée');
        this.searchPatient();
      },
      error: () => alert('Erreur prescription')
    });
  }

  // File input change handlers
  onScanFileChange(event: any) {
    this.scanFile = event.target.files[0];
  }

  onAnalysisFileChange(event: any) {
    this.analysisFile = event.target.files[0];
  }

  onSurgeryFileChange(event: any) {
    this.surgeryFile = event.target.files[0];
  }

  uploadScan() {
    if (!this.folderId || !this.scanFile) return;
    const formData = new FormData();
    formData.append('file', this.scanFile);
    formData.append('title', this.scanPayload.title);
    formData.append('description', this.scanPayload.description);
    this.doctorService.uploadScan(formData, this.folderId).subscribe({
      next: () => {
        alert('Scan ajouté');
        this.searchPatient();
        this.scanFile = null;
      },
      error: () => alert('Erreur ajout scan')
    });
  }

  uploadAnalysis() {
    if (!this.folderId || !this.analysisFile) return;
    const formData = new FormData();
    formData.append('file', this.analysisFile);
    formData.append('title', this.analysisPayload.title);
    formData.append('description', this.analysisPayload.description);
    this.doctorService.uploadAnalysis(formData, this.folderId).subscribe({
      next: () => {
        alert('Analyse ajoutée');
        this.searchPatient();
        this.analysisFile = null;
      },
      error: () => alert('Erreur ajout analyse')
    });
  }

  uploadSurgery() {
    if (!this.folderId) return;
    const formData = new FormData();
    formData.append('title', this.surgeryPayload.title);
    formData.append('description', this.surgeryPayload.description);
    if (this.surgeryFile) {
      formData.append('file', this.surgeryFile);
    }
    this.doctorService.uploadSurgery(formData, this.folderId).subscribe({
      next: () => {
        alert('Chirurgie ajoutée');
        this.searchPatient();
        this.surgeryFile = null;
      },
      error: () => alert('Erreur ajout chirurgie')
    });
  }

  viewScanDetails(id: number) {
    this.router.navigate(['/dashboard/doctor/scan', id]);
  }

  viewAnalysisDetails(id: number) {
    this.router.navigate(['/dashboard/doctor/analysis', id]);
  }

  viewSurgeryDetails(id: number) {
    this.router.navigate(['/dashboard/doctor/surgery', id]);
  }
}
