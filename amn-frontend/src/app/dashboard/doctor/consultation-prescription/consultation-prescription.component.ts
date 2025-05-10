import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  loading: boolean = false;
  success: boolean = false;

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
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.cin = params['cin'];
      this.fullName = params['fullName'];

      if (this.cin && this.fullName) {
        this.searchPatient();
      }
    });
  }

  searchPatient() {
    this.loading = true;
    this.doctorService.getPatientIdByCin(this.cin, this.fullName).subscribe({
      next: (id) => {
        this.patientId = id;
        this.loading = false;
      },
      error: () => {
        alert('Patient introuvable.');
        this.router.navigate(['/dashboard/doctor']);
        this.loading = false;
      }
    });
  }

  addMedication() {
    this.medications.push({ name: '', dosage: '', period: '', permanent: false });
  }

  removeMedication(index: number) {
    if (this.medications.length > 1) {
      this.medications.splice(index, 1);
    }
  }

  submit() {
    if (!this.patientId) {
      alert("Patient ID non trouvé. Veuillez rechercher le patient d'abord.");
      return;
    }

    if (!this.record.reason || !this.record.diagnosis) {
      alert("Veuillez remplir les champs Motif et Diagnostic.");
      return;
    }

    if (!this.medications[0].name || !this.medications[0].dosage) {
      alert("Veuillez ajouter au moins un médicament.");
      return;
    }

    this.loading = true;

    // Step 1: Create a Medical Record
    this.doctorService.createMedicalRecord(this.patientId, this.record).subscribe({
      next: (createdRecord) => {
        const recordId = createdRecord.id;

        // Step 2: Create Prescription and Link Medications
        const prescriptionRequest = {
          prescription: {
            medicationName: this.medications.length > 1 ? 'Multiple' : this.medications[0].name,
            dosage: this.medications.length > 1 ? '' : this.medications[0].dosage,
            period: this.medications.length > 1 ? '' : this.medications[0].period,
            permanent: this.medications.length > 1 ? false : this.medications[0].permanent,
            status: 'PENDING'
          },
          medications: this.medications
        };

        this.doctorService.submitPrescriptionWithMedications(
          this.patientId ?? 0, // Provide a default value (e.g., 0) if `this.patientId` is null
          recordId,
          prescriptionRequest
        ).subscribe({
          next: () => {
            this.loading = false;
            this.success = true;
            setTimeout(() => {
              this.router.navigate(['/dashboard/doctor']);
            }, 2000);
          },
          error: () => {
            this.loading = false;
            alert("Erreur lors de l'enregistrement de l'ordonnance.");
          }
        });

      },
      error: () => {
        this.loading = false;
        alert("Erreur lors de l'enregistrement de la consultation.");
      }
    });
  }

  cancel() {
    this.router.navigate(['/dashboard/doctor']);
  }
}
