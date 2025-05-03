import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-doctor',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './doctor.component.html'
})
export class DoctorComponent {
  cin = '';
  fullName = '';
  patientProfile: any = null;
  showManualForm = false; // ✅ declared here
  patientId: number | null = null;

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

  constructor(private doctorService: DoctorService) {}

  searchPatient() {
    this.doctorService.getFullPatientProfile(this.cin, this.fullName).subscribe({
      next: (profile) => {
        this.patientProfile = profile;
        this.showManualForm = false;
      },
      error: (err) => {
        if (err.status === 404) {
          const confirmCreate = confirm("Patient or dossier non trouvé. Voulez-vous créer et remplir les informations ?");
          if (confirmCreate) {
            this.doctorService.getPatientIdByCin(this.cin, this.fullName).subscribe({
              next: (id) => {
                this.patientId = id;
                this.showManualForm = true;
              },
              error: () => alert("Patient introuvable.")
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

    this.doctorService.createOrUpdateMedicalFolder(
      this.cin,
      this.fullName,
      this.manualData
    ).subscribe({
      next: () => {
        alert("Dossier créé et informations mises à jour.");
        this.showManualForm = false;
        this.searchPatient(); // Refresh after create
      },
      error: () => alert("Erreur lors de la création du dossier.")
    });
  }

  createMedicalRecord() {
    if (!this.patientProfile?.id) return;
    this.doctorService.createMedicalRecord(this.patientProfile.id, this.medicalRecord).subscribe({
      next: () => alert('Dossier médical ajouté'),
      error: () => alert('Erreur lors de l’ajout du dossier')
    });
  }

  writePrescription() {
    if (!this.patientProfile?.id) return;
    this.doctorService.writePrescription(this.patientProfile.id, this.prescription).subscribe({
      next: () => alert('Prescription ajoutée'),
      error: () => alert('Erreur lors de la prescription')
    });
  }
}
