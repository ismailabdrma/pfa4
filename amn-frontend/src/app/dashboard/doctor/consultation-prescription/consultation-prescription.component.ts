import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-consultation-prescription',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './consultation-prescription.component.html',
})
export class ConsultationPrescriptionComponent implements OnInit {
  cin: string = '';
  fullName: string = '';
  patientId: number | null = null;

  record = {
    reason: '',
    diagnosis: '',
    notes: ''
  };

  medications = [
    { name: '', dosage: '', period: '', permanent: false }
  ];

  constructor(
    private doctorService: DoctorService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.cin = params['cin'];
      this.fullName = params['fullName'];
      this.searchPatient();
    });
  }

  searchPatient() {
    if (!this.cin || !this.fullName) return;

    this.doctorService.getPatientIdByCin(this.cin, this.fullName).subscribe({
      next: id => {
        this.patientId = id;
      },
      error: () => alert('Patient introuvable.')
    });
  }

  addMedication() {
    this.medications.push({ name: '', dosage: '', period: '', permanent: false });
  }

  removeMedication(index: number) {
    this.medications.splice(index, 1);
  }

  submit() {
    if (!this.patientId) {
      alert("Veuillez d'abord chercher le patient.");
      return;
    }

    // Étape 1 : créer le dossier de consultation
    this.doctorService.createMedicalRecord(this.patientId, this.record).subscribe({
      next: (createdRecord) => {
        const recordId = createdRecord.id;

        // Étape 2 : créer la prescription liée à cette consultation
        const prescription = {
          medicationName: 'Ordonnance multiple',
          dosage: '',
          period: '',
          permanent: false,
          status: 'PENDING'
        };

        const request = {
          prescription,
          medications: this.medications
        };

        this.doctorService.submitPrescriptionWithMedications(this.patientId!, recordId, request).subscribe({
          next: () => alert('Consultation et prescription enregistrées.'),
          error: () => alert("Erreur lors de l'enregistrement de l'ordonnance.")
        });
      },
      error: () => alert("Erreur lors de l'enregistrement de la consultation.")
    });
  }
}
