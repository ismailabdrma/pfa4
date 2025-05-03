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
  medicalFolder: any = null;
  patientId: number | null = null;

  medicalRecord = {
    reason: '',
    diagnosis: '',
    notes: ''
  };

  prescription = {
    medicines: '',
    instructions: ''
  };

  constructor(private doctorService: DoctorService) {} // ✅ Inject the service

  searchPatient(cin: string, fullName: string) {
    this.doctorService.getMedicalFolder(cin, fullName).subscribe({
      next: (folder: any) => {
        this.medicalFolder = folder;
        this.patientId = folder?.patient?.id || null;
      },
      error: (err) => {
        if (err.status === 404) {
          const shouldCreate = confirm("Patient non trouvé. Voulez-vous créer un dossier médical ?");
          if (shouldCreate) {
            // 🔁 You need to create this endpoint in your backend or adjust logic
            alert("Le médecin ne peut pas créer un patient ici. Veuillez enregistrer le patient d'abord.");
          }
        }
      }
    });
  }

  createMedicalRecord() {
    if (!this.patientId) return;
    this.doctorService.createMedicalRecord(this.patientId, this.medicalRecord).subscribe({
      next: () => alert('Dossier médical ajouté avec succès'),
      error: () => alert('Erreur lors de l’ajout du dossier')
    });
  }

  writePrescription() {
    if (!this.patientId) return;
    this.doctorService.writePrescription(this.patientId, this.prescription).subscribe({
      next: () => alert('Prescription ajoutée avec succès'),
      error: () => alert('Erreur lors de la prescription')
    });
  }
}
